package io.github.moyugroup.auth.filter;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.common.util.PathUtil;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.orm.model.UserSession;
import io.github.moyugroup.auth.service.SSOLoginService;
import io.github.moyugroup.auth.service.TenantService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
            "/bootstrap/**",
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

    private final TenantService tenantService;

    public MoYuSSOLoginFilter(SSOLoginService ssoLoginService, TenantService tenantService) {
        this.ssoLoginService = ssoLoginService;
        this.tenantService = tenantService;
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
        String requestPath = httpRequest.getRequestURI();

        // 处理受保护且不在白名单的路径
        if (PathUtil.isMatch(requestPath, protectedPaths) && !PathUtil.isMatch(requestPath, whitePathList)) {
            log.debug("doFilter match path：{}", requestPath);
            // 匹配到登录保护路径，进行登录检查
            userLoginCheck(httpRequest, httpResponse, filterChain, requestPath);
        } else {
            log.debug("doFilter not match path：{}", requestPath);
            // 对于不匹配的路径，直接继续请求
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }

    /**
     * 登录检查
     *
     * @param httpRequest
     * @param httpResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    private void userLoginCheck(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain, String requestPath) throws IOException, ServletException {
        UserSession userSession = ssoLoginService.getUserLoginSession(httpRequest);
        if (Objects.nonNull(userSession)) {
            // 存在 userSession 则代表用户已经登录，建立用户登录上下文
            try {
                buildLoginContext(userSession);
                // 检查用户是否选择过租户
                if (loginTenantCheck(requestPath)) {
                    // 继续执行业务
                    filterChain.doFilter(httpRequest, httpResponse);
                } else {
                    // 跳转到选择租户页面 todo 携带 backUrl
                    httpResponse.sendRedirect(SSOLoginConstant.SWITCH_TENANT_PATH);
                }
            } finally {
                // 业务执行完毕后清理用户上下文
                UserContext.remove();
            }
        } else {
            log.debug("user is not logged in, redirect to {}", SSOLoginConstant.LOGIN_PAGE_PATH);
            // 用户未登录，跳转到登录页 todo 携带 backUrl
            httpResponse.sendRedirect(SSOLoginConstant.LOGIN_PAGE_PATH);
        }
    }

    /**
     * 检查是否存在租户信息
     *
     * @param requestPath
     * @return
     */
    private boolean loginTenantCheck(String requestPath) {
        // 跳过检查的页面
        if (PathUtil.isMatch(requestPath, Arrays.asList(SSOLoginConstant.SWITCH_TENANT_PATH, MoYuOAuthConstant.SWITCH_TENANT_ENDPOINT))) {
            return Boolean.TRUE;
        }
        // 检查用户上下文是否存在租户信息
        UserInfo userInfo = UserContext.get();
        if (StringUtils.isBlank(userInfo.getTenantId())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 通过 userSession 建立用户登录上下文
     *
     * @param userSession 用户登录态
     */
    private void buildLoginContext(UserSession userSession) {
        // 获取登录用户信息
        UserInfo userInfo = ssoLoginService.getUserByUserId(userSession.getUserId());
        if (StringUtils.isNoneBlank(userSession.getTenantId())) {
            userInfo.setTenantId(userSession.getTenantId());
            userInfo.setTenantName(tenantService.getTenantNameByTenantId(userSession.getTenantId()));
        }
        log.debug("user is logged in with UserInfo:{}", userInfo);
        UserContext.set(userInfo);
    }

    /**
     * 过滤器销毁逻辑
     */
    @Override
    public void destroy() {

    }

}
