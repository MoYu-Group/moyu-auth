package io.github.moyugroup.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 安全配置
 * <p>
 * Created by fanfan on 2023/07/01.
 */
@Configuration
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
        // 不使用 Spring Security 的登录认证能力，只使用安全防护能力
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                // csrf 白名单配置
                .csrf(csrf -> csrf.ignoringRequestMatchers("/open/**"));
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
