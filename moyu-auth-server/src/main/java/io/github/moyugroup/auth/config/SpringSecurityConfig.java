package io.github.moyugroup.auth.config;

import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.handler.MoYuServerAuthSuccessHandler;
import io.github.moyugroup.auth.handler.MoYuServerLogoutSuccessHandler;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.service.LoginCacheService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.MoYuAuthServerConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                                                          UserDetailsService userDetailsService,
                                                          HttpSessionRequestCache httpSessionRequestCache,
                                                          SessionRegistry sessionRegistry,
                                                          AppService appService,
                                                          MoYuServerAuthSuccessHandler moYuServerAuthSuccessHandler,
                                                          MoYuServerLogoutSuccessHandler moYuServerLogoutSuccessHandler
    ) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        // 不需要登录的端点和资源
                        authorize.requestMatchers(
                                        new AntPathRequestMatcher("/css/**"),
                                        new AntPathRequestMatcher("/favicon.ico"),
                                        new AntPathRequestMatcher("/open/**"),
                                        new AntPathRequestMatcher("/oauth2/**"),
                                        new AntPathRequestMatcher("/error"),
                                        new AntPathRequestMatcher("/health"),
                                        new AntPathRequestMatcher("/logged-out"),
                                        new AntPathRequestMatcher(MoYuOAuthConstant.LOGIN_PAGE_PATH),
                                        new AntPathRequestMatcher(MoYuOAuthConstant.LOGIN_ENDPOINT)
                                ).permitAll()
                                // 其他资源都需要登录
                                .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                // 自定义登录页
                .formLogin(form -> form
                        .loginPage(MoYuOAuthConstant.LOGIN_PAGE_PATH)
                        .failureUrl(MoYuOAuthConstant.LOGIN_PAGE_PATH)
                )
                // 自定义注销登录页
                .logout(logout -> logout
                        .logoutUrl(MoYuOAuthConstant.LOGIN_OUT_ENDPOINT)
                        .logoutSuccessHandler(moYuServerLogoutSuccessHandler)
                )
                // rememberMe token 24h 失效
                // TODO 服务端通过 token 刷新 session 后未更新 SessionRegistry 信息
                .rememberMe(config -> config
                        .alwaysRemember(true)
                        .tokenValiditySeconds(60 * 60 * 24)
                        .userDetailsService(userDetailsService)
                )
                .requestCache(cache -> cache.requestCache(httpSessionRequestCache))
                // session 并发控制和管理
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry)
                        .expiredUrl(MoYuOAuthConstant.LOGIN_PAGE_PATH)
                )
                .apply(new MoYuAuthServerConfigurer<>(
                        MoYuOAuthConstant.LOGIN_ENDPOINT,
                        MoYuOAuthConstant.LOGIN_PAGE_PATH,
                        appService,
                        moYuServerAuthSuccessHandler));
        ;
        return http.build();
    }

    @Bean
    public MoYuServerLogoutSuccessHandler moYuServerLogoutSuccessHandler(LoginCacheService loginCacheService) {
        return new MoYuServerLogoutSuccessHandler(loginCacheService);
    }

    /**
     * 初始化 MoYuAuthSuccessHandler
     *
     * @param loginCacheService
     * @return
     */
    @Bean
    public MoYuServerAuthSuccessHandler moYuAuthSuccessHandlerInit(LoginCacheService loginCacheService) {
        return new MoYuServerAuthSuccessHandler(loginCacheService);
    }

    /**
     * 配置密码解析器，使用 BCrypt 的方式对密码进行加密和验证
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO 登录过滤器暂时没用到这个
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
