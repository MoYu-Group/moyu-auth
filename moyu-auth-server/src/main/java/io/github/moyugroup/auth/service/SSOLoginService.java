package io.github.moyugroup.auth.service;

import cn.hutool.core.util.IdUtil;
import io.github.moyugroup.auth.constant.MoYuLoginConstant;
import io.github.moyugroup.auth.constant.enums.UserStatusEnum;
import io.github.moyugroup.auth.convert.UserConvert;
import io.github.moyugroup.auth.manage.UserManage;
import io.github.moyugroup.auth.manage.UserSessionManage;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.orm.model.UserSession;
import io.github.moyugroup.auth.pojo.dto.UserInfo;
import io.github.moyugroup.auth.util.CookieUtil;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.util.AssertUtil;
import io.github.moyugroup.web.util.WebUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * SSO 登录服务
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SSOLoginService {

    @Resource
    UserManage userManage;

    @Resource
    UserSessionManage userSessionManage;

    @Resource
    UserAsyncService userAsyncService;

    /**
     * 用户账户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param response response 对象
     */
    public void userLoginByAccount(String username, String password, HttpServletResponse response) {
        // 查询用户并判断用户是否存在
        User user = userManage.getUserByUsername(username);
        AssertUtil.notNull(user, ErrorCodeEnum.WRONG_USER_ACCOUNT_OR_PASSWORD);

        // 用户密码校验 todo 使用 Bcrypt 加密
        AssertUtil.isTrue(password.equals(user.getPassword()), ErrorCodeEnum.WRONG_USER_ACCOUNT_OR_PASSWORD);

        // 用户状态校验
        AssertUtil.isFalse(UserStatusEnum.LOCKED.equals(user.getUserStatus()), ErrorCodeEnum.USER_ACCOUNT_IS_LOCKED);
        AssertUtil.isFalse(UserStatusEnum.FROZEN.equals(user.getUserStatus()), ErrorCodeEnum.USER_ACCOUNT_IS_FROZEN);
        AssertUtil.isFalse(UserStatusEnum.RESTRICTED.equals(user.getUserStatus()), ErrorCodeEnum.USER_ACCOUNT_IS_RESTRICTED);

        // 构建用户中心登录态
        LocalDateTime loginTime = LocalDateTime.now();
        UserSession userSession = new UserSession()
                .setSessionId(IdUtil.fastSimpleUUID().toUpperCase())
                .setUserId(user.getUserId())
                .setCreatedTime(loginTime)
                .setExpiresTime(loginTime.plusSeconds(MoYuLoginConstant.LOGIN_EXPIRE_SECONDS))
                .setUserIp(WebUtil.getIpAddress())
                .setUserAgent(null);
        userSessionManage.userSessionSave(userSession);

        // 写入 Cookie
        CookieUtil.writeSSOLoginCookie(userSession.getSessionId(), response);

        // 异步更新用户最后登录时间
        userAsyncService.afterUserLogin(user.getUserId(), loginTime);

        log.debug("userLoginByAccount userSession:{}", userSession);
    }

    /**
     * 获取用户登录 Session
     *
     * @param request
     * @return
     */
    public UserSession getUserLoginSession(HttpServletRequest request) {
        String sessionId = CookieUtil.getSSOLoginSessionId(request);
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }
        return userSessionManage.getValidSessionBySessionId(sessionId);
    }

    /**
     * 根据用户ID获取登录用户信息
     *
     * @param userId
     * @return
     */
    public UserInfo getUserByUserId(String userId) {
        User user = userManage.getUserByUserId(userId);
        return UserConvert.INSTANCE.userToUserInfo(user);
    }

    /**
     * 用户注销登录
     *
     * @param request
     * @param response
     */
    public void userLogout(HttpServletRequest request, HttpServletResponse response) {
        UserSession userLoginSession = getUserLoginSession(request);
        if (Objects.nonNull(userLoginSession)) {
            // 删除用户登录 session
            log.debug("removeSessionById:{}", userLoginSession.getId());
            userSessionManage.removeSessionById(userLoginSession.getId());
            // 删除用户登录 cookie
            CookieUtil.removeSSOLoginCookie(response);
        }
    }
}
