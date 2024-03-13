package io.github.moyugroup.auth.filter;

import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.orm.model.UserSession;
import io.github.moyugroup.auth.pojo.dto.UserInfo;
import io.github.moyugroup.auth.service.SSOLoginService;
import io.github.moyugroup.auth.util.PathUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * MoYu SSO 登录过滤器
 * <p>
 * Created by fanfan on 2024/01/31.
 */
@Slf4j
public class MoYuSSOLoginFilter implements Filter {

    /**
     * 默认拦截路径
     */
    private final List<String> protectedPaths = List.of("/**");
    /**
     * 白名单路径，不拦截
     */
    private final List<String> whitePathList = Arrays.asList(
            "/css/**",
            "/js/**",
            "/static/**",
            "/favicon.ico",
            "/ssoLogin",
            "/ssoLogin.html",
            "/ssoLogout",
            "/oauth2/getUser",
            "/open/**"
    );

    private final SSOLoginService ssoLoginService;

    public MoYuSSOLoginFilter(SSOLoginService ssoLoginService) {
        this.ssoLoginService = ssoLoginService;
    }

    /**
     * 过滤器初始化逻辑
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("MoYuSSOLoginFilter init...");
    }

    /**
     * 过滤逻辑
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // 获取请求的路径
        String path = httpRequest.getRequestURI();

        // 处理受保护且不在白名单的路径
        if (PathUtil.isMatch(path, protectedPaths) && !PathUtil.isMatch(path, whitePathList)) {
            log.debug("doFilter match path：{}", path);
            UserSession userSession = ssoLoginService.getUserLoginSession(httpRequest);
            if (Objects.nonNull(userSession)) {
                try {
                    // 获取登录用户信息
                    UserInfo userInfo = ssoLoginService.getUserByUserId(userSession.getUserId());
                    log.debug("user is logged in with UserInfo:{}", userInfo);
                    UserContext.set(userInfo);
                    filterChain.doFilter(httpRequest, httpResponse);
                } finally {
                    UserContext.remove();
                }
            } else {
                log.debug("user is not logged in, redirect to {}", MoYuOAuthConstant.LOGIN_PAGE_PATH);
                // 用户未登录，跳转到登录页 todo 携带 backUrl
                httpResponse.sendRedirect(MoYuOAuthConstant.LOGIN_PAGE_PATH);
            }
        } else {
            log.debug("doFilter not match path：{}", path);
            // 对于不匹配的路径，直接继续请求
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }

    /**
     * 过滤器销毁逻辑
     */
    @Override
    public void destroy() {

    }

}
