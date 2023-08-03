package io.github.moyugroup.auth.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                .logout(LogoutConfigurer::permitAll)

        ;
        return http.build();
    }

    /**
     * 暂时配置一个基于内存的用户，框架在用户认证时会默认调用
     *
     * @param passwordEncoder 密码解析器
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails userDetails = User.withUsername("admin")
                .password(passwordEncoder.encode("123123"))
                .roles("USER")
                .authorities("app", "web")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
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
