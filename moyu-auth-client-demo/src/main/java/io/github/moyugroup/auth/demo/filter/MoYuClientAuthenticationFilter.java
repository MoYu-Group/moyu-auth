package io.github.moyugroup.auth.demo.filter;

import io.github.moyugroup.auth.demo.config.MoYuAuthClientProperties;
import io.github.moyugroup.auth.demo.config.MoYuOAuthConstant;
import io.github.moyugroup.auth.demo.constant.enums.GrantTypeEnum;
import io.github.moyugroup.auth.demo.pojo.vo.OAuth2UserVO;
import io.github.moyugroup.auth.demo.util.OAuth2HttpUtil;
import io.github.moyugroup.exception.AssertException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(MoYuOAuthConstant.OAUTH2_ENDPOINT, HttpMethod.GET.name());
    private final MoYuAuthClientProperties moYuAuthClientProperties;

    public MoYuClientAuthenticationFilter(MoYuAuthClientProperties moYuAuthClientProperties) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.moYuAuthClientProperties = moYuAuthClientProperties;
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
        saveSsoToken(request, ssoToken);
        // 通过 SSO_TOKEN 向 MoYu-Auth 获取登陆用户信息
        OAuth2UserVO loginUserByAccessToken = OAuth2HttpUtil.getLoginUserByAccessToken(getAccessTokenUrl(), moYuAuthClientProperties.getAppId(),
                moYuAuthClientProperties.getAppSecret(), GrantTypeEnum.AUTHORIZATION_CODE.getCode(), ssoToken);
        // 创建指定用户的登录态
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(loginUserByAccessToken, "", new ArrayList<>());
        setDetails(request, authenticated);
        return authenticated;
    }

    /**
     * 保存通过 SSO 登录获取的 ssoToken
     *
     * @param request
     */
    private void saveSsoToken(HttpServletRequest request, String ssoToken) {
        request.setAttribute(MoYuOAuthConstant.REQUEST_SSO_TOKEN_FIELD_NAME, ssoToken);
    }

    /**
     * 获取 MoYu-OAuth 服务器 AccessToken 校验地址
     * TODO 应用初始化时从服务器拉取对应信息
     *
     * @return
     */
    private String getAccessTokenUrl() {
        return moYuAuthClientProperties.getServerUrl() + MoYuOAuthConstant.OAUTH2_ACCESS_TOKEN_ENDPOINT;
    }

    private String getSSOToken(HttpServletRequest request) {
        String ssoToken = request.getParameter(MoYuOAuthConstant.SSO_TOKEN_PARAM);
        if (StringUtils.isBlank(ssoToken)) {
            throw new AssertException("ssoToken 不能为空");
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
