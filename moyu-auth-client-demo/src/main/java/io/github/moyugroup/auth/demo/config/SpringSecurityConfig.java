package io.github.moyugroup.auth.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.MoYuAuthConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
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

    public static final String LOGIN_PAGE_URL = "/login.html";
    public static final String LOGIN_PAGE_API = "/login";
    public static final String LOGIN_OUT_API = "/logout";

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/favicon.ico");
    }

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
                                                          SessionRegistry sessionRegistry
    ) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        // 不需要登录的端点
                        authorize.requestMatchers(
                                        new AntPathRequestMatcher("/open/**"),
                                        new AntPathRequestMatcher("/error"),
                                        new AntPathRequestMatcher("/health"),
                                        new AntPathRequestMatcher("/logged-out"),
                                        new AntPathRequestMatcher("/authorized"),
                                        new AntPathRequestMatcher(LOGIN_PAGE_URL),
                                        new AntPathRequestMatcher(LOGIN_PAGE_API)).permitAll()
                                // 其他资源都需要登录
                                .anyRequest().authenticated())
                .csrf(x -> x.disable())
                // 自定义登录页
                .formLogin(form -> form
                        .loginPage(LOGIN_PAGE_URL)
                        .failureUrl(LOGIN_PAGE_URL)
                        .loginProcessingUrl(LOGIN_PAGE_API).disable()
                )
                // 自定义注销登录页
                .logout(logout -> logout
                        .logoutUrl(LOGIN_OUT_API)
                        .logoutSuccessUrl(LOGIN_PAGE_URL)
                )
                // rememberMe token 24h 失效
                .rememberMe(config -> config
                        .alwaysRemember(true)
                        .tokenValiditySeconds(60 * 60 * 24)
                )
                .requestCache(cache -> cache.requestCache(httpSessionRequestCache))
                // session 并发控制和管理
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry)
                        .expiredUrl(LOGIN_PAGE_URL)
                )
                // MoYu 认证配置
                .apply(new MoYuAuthConfigurer<>())
        ;
        DefaultSecurityFilterChain build = http.build();
        return build;
    }

    /**
     * 配置密码解析器，使用 BCrypt 的方式对密码进行加密和验证
     *
     * @return BCryptPasswordEncoder
     */
//    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
