package io.github.moyugroup.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        // 不需要登录端点和资源
                        authorize.requestMatchers(
                                        new AntPathRequestMatcher("/open/**"),
                                        new AntPathRequestMatcher("/error"),
                                        new AntPathRequestMatcher("/health"),
                                        new AntPathRequestMatcher("/ssoLogin.html"),
                                        new AntPathRequestMatcher("/**/*.ico"),
                                        new AntPathRequestMatcher("/**/*.jpg"),
                                        new AntPathRequestMatcher("/css/**")).permitAll()
                                // 其他资源都需要登录
                                .anyRequest().authenticated())
                .csrf(Customizer.withDefaults())
                // 自定义登录页
                .formLogin(form -> form
                        .loginPage("/ssoLogin.html")
                        .loginProcessingUrl("/ssoLogin").permitAll()
                )
                .logout(logout -> logout.logoutUrl("/ssoLogout"))
                .rememberMe(config -> config
                        .alwaysRemember(true)
                        .tokenValiditySeconds(60 * 60 * 24))
        ;
        return http.build();
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

}
