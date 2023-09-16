package io.github.moyugroup.auth.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 摸鱼登录 属性配置
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "moyu.auth.client")
public class MoYuAuthClientProperties {
    /**
     * 客户端应用appId
     */
    private String appId;
    /**
     * 客户端应用密钥
     */
    private String appSecret;
    /**
     * 登录授权服务器地址
     */
    private String serverUrl;
}
