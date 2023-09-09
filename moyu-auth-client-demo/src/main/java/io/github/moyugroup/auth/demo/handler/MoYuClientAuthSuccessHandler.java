package io.github.moyugroup.auth.demo.handler;

import io.github.moyugroup.auth.demo.config.MoYuOAuthConstant;
import io.github.moyugroup.auth.demo.service.LoginCacheService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * MoYu-Auth 登录客户端，登录成功后统一处理器
 * <p>
 * Created by fanfan on 2023/09/09.
 */
@Slf4j
public class MoYuClientAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private LoginCacheService loginCacheService;

    /**
     * 构造，用于注入 LoginCacheService
     *
     * @param loginCacheService
     */
    public MoYuClientAuthSuccessHandler(LoginCacheService loginCacheService) {
        this.loginCacheService = loginCacheService;
    }

    /**
     * 登录成功后的处理
     *
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 保存通过 ssoToken 登录的用户 sessionId 映射信息，供退出登录时使用
        String ssoToken = getRequestToken(request);
        String requestSessionId = getRequestSessionId(request);
        loginCacheService.saveSsoTokenAndSessionId(ssoToken, requestSessionId);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * 获取 ssoToken
     *
     * @param request
     * @return
     */
    private String getRequestToken(HttpServletRequest request) {
        return (String) request.getAttribute(MoYuOAuthConstant.REQUEST_SSO_TOKEN_FIELD_NAME);
    }

    /**
     * 获取 SessionId
     *
     * @param request
     * @return
     */
    private String getRequestSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

}
