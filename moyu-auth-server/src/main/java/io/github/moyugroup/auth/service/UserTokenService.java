package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.constant.enums.SSOLoginErrorEnum;
import io.github.moyugroup.auth.constant.enums.TokenTypeEnum;
import io.github.moyugroup.auth.manage.TenantManage;
import io.github.moyugroup.auth.manage.UserManage;
import io.github.moyugroup.auth.manage.UserSessionManage;
import io.github.moyugroup.auth.manage.UserTokenManage;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.orm.model.UserSession;
import io.github.moyugroup.auth.orm.model.UserToken;
import io.github.moyugroup.auth.util.KeyGeneratorUtil;
import io.github.moyugroup.util.AssertUtil;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户令牌服务
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTokenService {

    @Resource
    UserTokenManage userTokenManage;

    @Resource
    UserSessionManage userSessionManage;

    @Resource
    UserManage userManage;

    @Resource
    TenantManage tenantManage;

    /**
     * 生成用户 accessToken
     * 有效期5分钟
     *
     * @param sessionId
     * @return
     */
    public String generateSSOToken(String sessionId) {
        String accessToken = KeyGeneratorUtil.generateToken();
        UserToken userToken = new UserToken()
                .setSessionId(sessionId)
                .setAccessToken(accessToken)
                .setTokenType(TokenTypeEnum.BEARER_TOKEN)
                .setExpireTime(LocalDateTime.now().plusMinutes(5));
        userTokenManage.userTokenSave(userToken);
        return accessToken;
    }

    /**
     * 根据 ssoToken 获取登录用户信息
     * todo app sso 登录 限制
     *
     * @param ssoToken
     * @return
     */
    public UserInfo getUserBySSOToken(String ssoToken) {
        // 获取有效 Token
        UserToken userToken = userTokenManage.getValidUserTokenByAccessToken(ssoToken);
        AssertUtil.notNull(userToken, SSOLoginErrorEnum.SSO_TOKEN_INVALID);
        // 获取成功后删除 UserToken
//        userTokenManage.removeUserTokenById(userToken.getId());

        // 获取用户登录session
        UserSession userSession = userSessionManage.getValidSessionBySessionId(userToken.getSessionId());
        AssertUtil.notNull(userSession, SSOLoginErrorEnum.GET_USER_SESSION_ERROR);
        String tenantName = tenantManage.getTenantNameByTenantId(userSession.getTenantId());

        // 获取登录用户信息
        User user = userManage.getUserByUserId(userSession.getUserId());
        AssertUtil.notNull(userSession, SSOLoginErrorEnum.GET_USER_INFO_ERROR);

        // 构建用户登录信息返回
        UserInfo userInfo = new UserInfo();
        userInfo.setTenantId(userSession.getTenantId())
                .setTenantName(tenantName)
                .setUserId(user.getUserId())
                .setUsername(user.getUsername())
                .setNickname(user.getNickname());
        // todo 用户敏感信息需要用户授权后返回
        return userInfo;
    }

}
