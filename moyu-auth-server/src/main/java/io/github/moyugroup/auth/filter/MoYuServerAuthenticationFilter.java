package io.github.moyugroup.auth.filter;

import io.github.moyugroup.auth.constant.MoYuAuthConstant;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.service.AppService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Objects;

/**
 * 自定义登录过程过滤器
 * <p>
 * Created by fanfan on 2023/09/04.
 */
public class MoYuServerAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final AppService appService;

    public MoYuServerAuthenticationFilter(String loginPage, AppService appService) {
        super(new AntPathRequestMatcher(loginPage, HttpMethod.POST.name()));
        this.appService = appService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = getUsername(request);
        String password = getPassword(request);
        checkParams(request, username, password);
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private void checkParams(HttpServletRequest request, String username, String password) {
        if (StringUtils.isBlank(username)) {
            throw new BadCredentialsException("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            throw new BadCredentialsException("密码不能为空");
        }
        String appId = request.getParameter(MoYuAuthConstant.APP_ID_PARAM);
        if (StringUtils.isNotBlank(appId)) {
            AppVO appById = appService.getAppById(appId);
            if (Objects.isNull(appById)) {
                throw new BadCredentialsException("应用未在统一登录中心注册");
            }
            request.setAttribute(MoYuAuthConstant.REQUEST_APP_INFO, appById);
        }
    }

    private String getUsername(HttpServletRequest request) {
        String username = request.getParameter(MoYuAuthConstant.LOGIN_USERNAME_PARAM);
        username = (username != null) ? username.trim() : "";
        return username;
    }

    private String getPassword(HttpServletRequest request) {
        String password = request.getParameter(MoYuAuthConstant.LOGIN_PASSWORD_PARAM);
        password = (password != null) ? password : "";
        return password;
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication
     * request's details property.
     *
     * @param request     that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     *                    set
     */
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
