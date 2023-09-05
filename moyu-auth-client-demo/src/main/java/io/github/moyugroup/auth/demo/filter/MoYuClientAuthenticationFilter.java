package io.github.moyugroup.auth.demo.filter;

import io.github.moyugroup.auth.demo.config.MoYuAuthConstant;
import io.github.moyugroup.exception.AssertException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
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
@Slf4j
public class MoYuClientAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 授权回调地址
     */
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(MoYuAuthConstant.OAUTH_ENDPOINT, HttpMethod.GET.name());

    public MoYuClientAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
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
        String ssoToken = getSSOToken(request);
        log.info("attemptAuthentication ssoToken:{}", ssoToken);
        // 通过 SSO_TOKEN 向 MoYu-Auth 获取登陆用户信息

        // 创建指定用户的登录态
        User user = new User("username", "password", new ArrayList<>());
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, "password", new ArrayList<>());
        setDetails(request, authenticated);
        return authenticated;
    }

    private String getSSOToken(HttpServletRequest request) {
        String ssoToken = request.getParameter(MoYuAuthConstant.SSO_TOKEN_PARAM);
        if (StringUtils.isBlank(ssoToken)) {
            throw new AssertException("SSO_TOKEN 不能为空");
        }
        return ssoToken.trim();
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
