package io.github.moyugroup.auth.model.bo;

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
     * 应用登录token
     */
    private String ssoToken;
}
