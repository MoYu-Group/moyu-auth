package io.github.moyugroup.auth.handler;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.bo.UserLoginAppBO;
import io.github.moyugroup.auth.pojo.vo.LoginUserVO;
import io.github.moyugroup.auth.service.LoginCacheService;
import io.github.moyugroup.auth.util.OAuth2HttpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * MoYu-Auth 登录中心，注销成功后的统一处理
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
public class MoYuServerLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private LoginCacheService loginCacheService;

    /**
     * 构造，用于注入 LoginCacheService
     *
     * @param loginCacheService
     */
    public MoYuServerLogoutSuccessHandler(LoginCacheService loginCacheService) {
        this.loginCacheService = loginCacheService;
    }

    /**
     * 注销成功后的逻辑重写
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            LoginUserVO userVO = (LoginUserVO) principal;
            List<UserLoginAppBO> userLoginAppList = loginCacheService.getUserLoginAppList(userVO.getUserId());
            if (!CollectionUtils.isEmpty(userLoginAppList)) {
                // 遍历用户登录的应用列表，通知应用用户已注销
                for (UserLoginAppBO userLoginAppBO : userLoginAppList) {
                    sendNotifyAppUserLogoutByToken(getAppLogoutUrl(userLoginAppBO), userLoginAppBO.getSsoToken());
                }
            }
        }
        super.onLogoutSuccess(request, response, authentication);
    }

    private void sendNotifyAppUserLogoutByToken(String url, String ssoToken) {
        try {
            OAuth2HttpUtil.notifyAppUserLogoutByToken(url, ssoToken);
        } catch (Exception ex) {
            log.error("send UserLogout Notify To App Error:{}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 获取应用注销地址
     *
     * @param userLoginAppBO
     * @return
     */
    private String getAppLogoutUrl(UserLoginAppBO userLoginAppBO) {
        UrlBuilder callBackUrlBuilder = UrlBuilder.of(userLoginAppBO.getAppUrl());
        String logoutCallbackUrl = userLoginAppBO.getLogoutCallbackPath();
        if (StringUtils.isBlank(logoutCallbackUrl)) {
            // 应用未配置指定回调路径，使用系统默认注销回调路径
            callBackUrlBuilder.setPath(UrlPath.of(MoYuOAuthConstant.LOGIN_OUT_ENDPOINT, Charset.defaultCharset()));
        } else {
            // 使用用于指定的注销回调路径
            callBackUrlBuilder.setPath(UrlPath.of(logoutCallbackUrl, Charset.defaultCharset()));
        }
        return callBackUrlBuilder.toString();
    }
}
