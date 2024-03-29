package io.github.moyugroup.auth.demo.filter;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.common.util.PathUtil;
import io.github.moyugroup.auth.demo.config.MoYuAuthClientProperties;
import io.github.moyugroup.auth.demo.util.SSOLoginUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 应用端 SSO 登录过滤器
 * <p>
 * Created by fanfan on 2024/03/29.
 */
@Slf4j
public class MoYuClientSSOLoginFilter implements Filter {

    /**
     * 默认拦截路径
     */
    private final List<String> protectedPaths = List.of("/**");

    /**
     * 白名单路径，不拦截
     * todo 后续可从应用配置读取
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

    private final MoYuAuthClientProperties properties;

    public MoYuClientSSOLoginFilter(MoYuAuthClientProperties moYuAuthClientProperties) {
        this.properties = moYuAuthClientProperties;
    }

    /**
     * 过滤器初始化逻辑
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.debug("MoYuClientSSOLoginFilter init...");
    }

    /**
     * 过滤逻辑
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取请求的路径
        String requestPath = httpRequest.getRequestURI();

        // 处理受保护且不在白名单的路径
        if (PathUtil.isMatch(requestPath, protectedPaths) && !PathUtil.isMatch(requestPath, whitePathList)) {
            log.debug("doFilter match path：{}", requestPath);
            // 匹配到登录保护路径，进行登录检查
            userLoginCheck(httpRequest, httpResponse, filterChain);
        } else {
            log.debug("doFilter not match path：{}", requestPath);
            // 对于不匹配的路径，直接继续请求
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }

    /**
     * 用户登录检查
     *
     * @param httpRequest
     * @param httpResponse
     * @param filterChain
     */
    private void userLoginCheck(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws ServletException, IOException {
        UserInfo userInfo = SSOLoginUtil.getUserFromCookie(httpRequest, properties.getAppId(), properties.getAppSecret());
        if (Objects.nonNull(userInfo)) {
            // 建立用户登录上下文
            buildLoginContext(userInfo);
            // 继续执行业务
            filterChain.doFilter(httpRequest, httpResponse);
        } else {
            String backUrl = SSOLoginUtil.getFullUrl(httpRequest);
            String appId = properties.getAppId();
            String redirectUrl = properties.getServerUrl() + SSOLoginConstant.LOGIN_PAGE_PATH
                    + "?" + SSOLoginConstant.APP_ID + "=" + appId
                    + "&" + SSOLoginConstant.BACK_URL + "=" + URLEncoder.encode(backUrl, StandardCharsets.UTF_8);
            // 用户未登录，跳转到sso登录页面
            log.debug("user is not logged in, redirect to {}", redirectUrl);
            httpResponse.sendRedirect(redirectUrl);
        }
    }

    /**
     * 建立用户登录上下文
     *
     * @param userInfo 用户信息
     */
    private void buildLoginContext(UserInfo userInfo) {
        log.debug("user is logged in with UserInfo:{}", userInfo);
        UserContext.set(userInfo);
    }

    /**
     * 过滤器销毁逻辑
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
