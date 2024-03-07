package io.github.moyugroup.auth.filter;

import io.github.moyugroup.auth.util.PathUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

        // 路径拦截路径，且不在白名单
        if (PathUtil.isMatch(path, protectedPaths) && !PathUtil.isMatch(path, whitePathList)) {
            log.debug("doFilter match path：{}", path);
            if (checkIsLogin(httpRequest)) {
                // 用户已登录，继续请求
                log.debug("user is logged in");
                filterChain.doFilter(httpRequest, httpResponse);
            } else {
                log.debug("user is not logged in, redirect to /ssoLogin.html");
                // 用户未登录，跳转到登录页 todo 携带 backUrl
                httpResponse.sendRedirect("/ssoLogin.html");
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

    /**
     * 检查用户是否登录
     *
     * @param httpRequest
     * @return
     */
    private boolean checkIsLogin(HttpServletRequest httpRequest) {
        Cookie[] cookies = httpRequest.getCookies();
        return false;
    }

}
