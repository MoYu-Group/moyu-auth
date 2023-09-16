package io.github.moyugroup.auth.demo.config;

import io.github.moyugroup.auth.demo.handler.MoYuAuthenticationEntryPoint;
import io.github.moyugroup.auth.demo.handler.MoYuClientAuthSuccessHandler;
import io.github.moyugroup.auth.demo.handler.MoYuClientLogoutSuccessHandler;
import io.github.moyugroup.auth.demo.service.LoginCacheService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.MoYuAuthClientConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security 安全配置
 * <p>
 * Created by fanfan on 2023/07/01.
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(MoYuAuthClientProperties.class)
public class SpringSecurityConfig {

    /**
     * 用于身份验证的 Spring Security 过滤器链
     *
     * @param http security 核心配置类
     * @return 过滤器链
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          HttpSessionRequestCache httpSessionRequestCache,
                                                          SessionRegistry sessionRegistry,
                                                          MoYuAuthClientProperties moYuAuthClientProperties,
                                                          MoYuClientAuthSuccessHandler moYuClientAuthSuccessHandler,
                                                          MoYuClientLogoutSuccessHandler moYuClientLogoutSuccessHandler
    ) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        // 不需要登录的端点
                        authorize.requestMatchers(
                                        new AntPathRequestMatcher("/css/**"),
                                        new AntPathRequestMatcher("/favicon.ico"),
                                        new AntPathRequestMatcher("/open/**"),
                                        new AntPathRequestMatcher("/error"),
                                        new AntPathRequestMatcher("/health"),
                                        new AntPathRequestMatcher("/logged-out"),
                                        new AntPathRequestMatcher(MoYuOAuthConstant.OAUTH2_ENDPOINT)
                                ).permitAll()
                                // 其他资源都需要登录
                                .anyRequest().authenticated())
                .csrf(x -> x.disable())
                // 自定义应用注销配置
                .logout(logout -> logout
                        .logoutUrl(MoYuOAuthConstant.LOGOUT_ENDPOINT)
                        .logoutSuccessUrl("/")
                        .logoutSuccessHandler(moYuClientLogoutSuccessHandler)
                )
                .requestCache(cache -> cache.requestCache(httpSessionRequestCache))
                // session 并发控制和管理
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry)
                        // session 失效后跳转的页面，待优化，TODO 应该跳转到登录页，附带 backUrl
                        .expiredUrl("/")
                )
                // 异常处理
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new MoYuAuthenticationEntryPoint(moYuAuthClientProperties))
                )
                // MoYu 认证配置
                .apply(new MoYuAuthClientConfigurer<>(moYuAuthClientProperties, moYuClientAuthSuccessHandler))
        ;
        return http.build();
    }

    @Bean
    public MoYuClientAuthSuccessHandler moYuClientAuthSuccessHandlerInit(LoginCacheService loginCacheService) {
        return new MoYuClientAuthSuccessHandler(loginCacheService);
    }

    @Bean
    public MoYuClientLogoutSuccessHandler moYuClientLogoutSuccessHandlerInit(LoginCacheService loginCacheService, SessionRegistry sessionRegistry) {
        return new MoYuClientLogoutSuccessHandler(loginCacheService, sessionRegistry);
    }

    @Bean
    public HttpSessionRequestCache httpSessionRequestCache() {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        return requestCache;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}
