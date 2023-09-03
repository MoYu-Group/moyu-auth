package io.github.moyugroup.auth.demo.filter;

import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;

/**
 * 自定义登录过程
 * <p>
 * Created by fanfan on 2023/08/11.
 */
public class MoYuClientAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";

    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    /**
     * 授权回调地址
     */
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/authorized", "GET");

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

    private boolean postOnly = true;

    public MoYuClientAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    public MoYuClientAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    /**
     * 认证过程
     *
     * @param request  从中提取参数并执行身份验证
     * @param response 如果实现必须将重定向作为多阶段身份验证过程的一部分(如OIDC)，则可能需要此response
     * @return
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals("GET")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = request.getParameter("username");
        username = (username != null) ? username.trim() : "";
        String password = request.getParameter("password");
        password = (password != null) ? password : "";
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BizException(ErrorCodeEnum.USERNAME_VERIFICATION_FAILED);
        }
        // 创建认证通过的对象，目前免认证登录，所有账密都可以登录，后续会调用 MoYu-Auth 认证登录
        User user = new User(username, password, new ArrayList<>());
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, password, new ArrayList<>());
        setDetails(request, authenticated);
        return authenticated;
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
