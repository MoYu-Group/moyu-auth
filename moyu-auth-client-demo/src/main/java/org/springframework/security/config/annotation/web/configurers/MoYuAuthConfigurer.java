/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.config.annotation.web.configurers;

import io.github.moyugroup.auth.demo.filter.MoYuClientAuthenticationFilter;
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
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.util.Arrays;
import java.util.Collections;

/**
 * MoYu 自定义过滤器配置
 *
 * @param <H>
 */
public class MoYuAuthConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<MoYuAuthConfigurer<H>, H> {

    /**
     * 自定义过滤器不再泛化
     */
    private final MoYuClientAuthenticationFilter authFilter;
    /**
     * 默认使用保存请求认证成功的处理器
     */
    private final SavedRequestAwareAuthenticationSuccessHandler defaultSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    /**
     * 保存认证请求细节
     */
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    /**
     * 认证成功处理器
     */
    private AuthenticationSuccessHandler successHandler = this.defaultSuccessHandler;
    /**
     * 登录认证端点
     */
    private LoginUrlAuthenticationEntryPoint authenticationEntryPoint;
    /**
     * 是否自定义页面
     */
    private boolean customLoginPage;
    /**
     * 登录页面
     */
    private String loginPage;
    /**
     * 登录成功URL
     */
    private String loginProcessingUrl;
    /**
     * 认证失败处理器
     */
    private AuthenticationFailureHandler failureHandler;
    /**
     * 认证路径是否放开
     */
    private boolean permitAll;
    /**
     * 认证失败的URL
     */
    private String failureUrl;

    /**
     * 无参构造，初始化过滤器
     */
    public MoYuAuthConfigurer() {
        setLoginPage("/authorized");
        this.authFilter = new MoYuClientAuthenticationFilter();
    }

    /**
     * Create the {@link RequestMatcher} given a loginProcessingUrl
     *
     * @param loginProcessingUrl creates the {@link RequestMatcher} based upon the
     *                           loginProcessingUrl
     * @return the {@link RequestMatcher} to use based upon the loginProcessingUrl
     */
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public MoYuAuthConfigurer<H> successHandler(String forwardUrl) {
        this.successHandler = new ForwardAuthenticationSuccessHandler(forwardUrl);
        return this;
    }

    @Override
    public void init(H http) throws Exception {
        this.failureHandler = new SimpleUrlAuthenticationFailureHandler(loginPage);
        registerDefaultAuthenticationEntryPoint((HttpSecurity) http);
    }

    @SuppressWarnings("unchecked")
    protected final void registerDefaultAuthenticationEntryPoint(HttpSecurity http) {
        registerAuthenticationEntryPoint(http, this.authenticationEntryPoint);
    }

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
        RequestCache requestCache = http.getSharedObject(RequestCache.class);
        if (requestCache != null) {
            this.defaultSuccessHandler.setRequestCache(requestCache);
        }
        this.authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        this.authFilter.setAuthenticationSuccessHandler(this.successHandler);
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
        SecurityContextConfigurer<H> securityContextConfigurer = http.getConfigurer(SecurityContextConfigurer.class);
        if (securityContextConfigurer != null && securityContextConfigurer.isRequireExplicitSave()) {
            SecurityContextRepository securityContextRepository = securityContextConfigurer
                    .getSecurityContextRepository();
            this.authFilter.setSecurityContextRepository(securityContextRepository);
        }
        this.authFilter.setSecurityContextHolderStrategy(getSecurityContextHolderStrategy());
        MoYuClientAuthenticationFilter filter = postProcess(this.authFilter);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    protected MoYuAuthConfigurer<H> loginPage(String loginPage) {
        setLoginPage(loginPage);
        this.customLoginPage = true;
        return this;
    }


    private void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
        this.authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginPage);
    }

}
