package io.github.moyugroup.auth.pojo.bo;

import lombok.Data;

/**
 * 用户登录的应用对象
 * <p>
 * Created by fanfan on 2023/09/09.
 */
@Data
public class UserLoginAppBO {

    /**
     * 应用ID
     */
    private String appId;
    /**
     * 应用访问地址
     */
    private String appUrl;
    /**
     * 应用退出登录回调路径
     */
    private String logoutCallbackPath;
    /**
     * 应用登录token
     */
    private String ssoToken;
}
