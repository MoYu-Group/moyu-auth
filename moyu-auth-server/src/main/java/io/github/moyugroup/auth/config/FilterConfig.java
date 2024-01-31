package io.github.moyugroup.auth.config;

import io.github.moyugroup.auth.filter.MoYuSSOLoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 * <p>
 * Created by fanfan on 2024/01/31.
 */
@Slf4j
@Configuration
public class FilterConfig {

    /**
     * 过滤器注册
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<MoYuSSOLoginFilter> ssoFilterRegistration() {
        FilterRegistrationBean<MoYuSSOLoginFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MoYuSSOLoginFilter());
        // 配置过滤器的路径
        registration.addUrlPatterns("/*");
        // 设置过滤器的顺序
        registration.setOrder(1);
        return registration;
    }

}
