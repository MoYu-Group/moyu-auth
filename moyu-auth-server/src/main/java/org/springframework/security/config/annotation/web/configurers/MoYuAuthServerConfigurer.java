package org.springframework.security.config.annotation.web.configurers;

import io.github.moyugroup.auth.filter.MoYuServerAuthenticationFilter;
import io.github.moyugroup.auth.handler.MoYuAuthSuccessHandler;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.strategy.MoYuAuthRedirectStrategy;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.util.Arrays;
import java.util.Collections;

/**
 * MoYu-Auth 服务端自定义过滤器配置
 * <p>
 * Created by fanfan on 2023/09/04.
 */
public class MoYuAuthServerConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<MoYuAuthServerConfigurer<H>, H> {

    /**
     * 自定义过滤器
     */
    private final MoYuServerAuthenticationFilter authFilter;
    /**
     * 登录成功处理器
     */
    private MoYuAuthSuccessHandler moYuAuthSuccessHandler;
    /**
     * 认证失败处理器
     */
    private final AuthenticationFailureHandler failureHandler;
    /**
     * 保存认证请求细节
     */
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    /**
     * 登录认证端点
     */
    private LoginUrlAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 无参构造，初始化过滤器
     */
    public MoYuAuthServerConfigurer(String loginEndPoint, String loginUrl, AppService appService, MoYuAuthSuccessHandler moYuAuthSuccessHandler) {
        this.failureHandler = getFailureHandler(loginUrl);
        this.authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginEndPoint);
        this.authFilter = new MoYuServerAuthenticationFilter(loginEndPoint, appService);
        this.moYuAuthSuccessHandler = moYuAuthSuccessHandler;
    }

    private AuthenticationFailureHandler getFailureHandler(String loginUrl) {
        SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler(loginUrl);
        simpleUrlAuthenticationFailureHandler.setUseForward(false);
        // 登录失败时，使用 MoYu 重定向策略
        simpleUrlAuthenticationFailureHandler.setRedirectStrategy(new MoYuAuthRedirectStrategy());
        return simpleUrlAuthenticationFailureHandler;
    }

    /**
     * 配置初始化
     *
     * @param http
     */
    @Override
    public void init(H http) {
        // 注册认证人口
        registerAuthenticationEntryPoint((HttpSecurity) http, this.authenticationEntryPoint);
    }

    /**
     * 认证入口配置
     *
     * @param http
     * @param authenticationEntryPoint
     */
    @SuppressWarnings("unchecked")
    protected final void registerAuthenticationEntryPoint(HttpSecurity http, AuthenticationEntryPoint authenticationEntryPoint) {
        ExceptionHandlingConfigurer<H> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        exceptionHandling.defaultAuthenticationEntryPointFor(postProcess(authenticationEntryPoint),
                getAuthenticationEntryPointMatcher(http));
    }

    protected final RequestMatcher getAuthenticationEntryPointMatcher(HttpSecurity http) {
        ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher mediaMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy,
                MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN);
        mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        RequestMatcher notXRequestedWith = new NegatedRequestMatcher(
                new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
        return new AndRequestMatcher(Arrays.asList(notXRequestedWith, mediaMatcher));
    }

    @Override
    public void configure(H http) {
        PortMapper portMapper = http.getSharedObject(PortMapper.class);
        if (portMapper != null) {
            this.authenticationEntryPoint.setPortMapper(portMapper);
        }
        this.authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        this.authFilter.setAuthenticationSuccessHandler(moYuAuthSuccessHandler);
        this.authFilter.setAuthenticationFailureHandler(this.failureHandler);
        if (this.authenticationDetailsSource != null) {
            this.authFilter.setAuthenticationDetailsSource(this.authenticationDetailsSource);
        }
        SessionAuthenticationStrategy sessionAuthenticationStrategy = http
                .getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            this.authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }
        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            this.authFilter.setRememberMeServices(rememberMeServices);
        }
        SecurityContextConfigurer securityContextConfigurer = http.getConfigurer(SecurityContextConfigurer.class);
        if (securityContextConfigurer != null && securityContextConfigurer.isRequireExplicitSave()) {
            SecurityContextRepository securityContextRepository = securityContextConfigurer.getSecurityContextRepository();
            this.authFilter.setSecurityContextRepository(securityContextRepository);
        }
        this.authFilter.setSecurityContextHolderStrategy(getSecurityContextHolderStrategy());
        MoYuServerAuthenticationFilter filter = postProcess(this.authFilter);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

}
