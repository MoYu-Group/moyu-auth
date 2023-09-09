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
     * APP标识
     */
    private String appId;
    /**
     * APP密钥
     */
    private String appSecret;
    /**
     * APP访问地址
     */
    private String appUrl;
    /**
     * APP登陆成功SSO回调路径
     */
    private String ssoCallbackPath;
    /**
     * APP退出登录回调路径
     */
    private String logoutCallbackPath;

}
