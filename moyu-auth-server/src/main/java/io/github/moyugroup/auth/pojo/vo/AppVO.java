package io.github.moyugroup.auth.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * APP 对象
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Setter
@Getter
@Accessors(chain = true)
public class AppVO {

    /**
     * 应用ID
     */
    String appId;
    /**
     * 应用名称
     */
    String appName;
    /**
     * 应用描述
     */
    String appDescription;
    /**
     * 应用密钥
     */
    String appSecret;
    /**
     * 重定向地址
     */
    String redirectUri;
    /**
     * 是否启用
     */
    Boolean enabled;
}
