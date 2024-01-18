package io.github.moyugroup.auth.demo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 安全配置
 * <p>
 * Created by fanfan on 2023/07/01.
 */
@Configuration
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
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 不使用 Spring Security 的登录认证能力，只使用安全防护能力
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                // csrf 白名单配置
//                .csrf(csrf -> csrf.ignoringRequestMatchers("/open/**"))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

}
