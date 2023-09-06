package io.github.moyugroup.auth.demo.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Moyu-Auth 常量类
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Setter
@Getter
public class MoYuAuthConstant {

    public final static String APP_ID_PARAM = "appId";
    public final static String APP_SECRET_PARAM = "appSecret";
    public final static String BACK_URL_PARAM = "backUrl";
    public final static String SSO_TOKEN_PARAM = "ssoToken";
    public final static String GRANT_TYPE_PARAM = "grantType";
    public final static String LOGIN_IN_URL = "/ssoLogin.html";
    public final static String LOGIN_OUT_URL = "/ssoLogout.html";
    public final static String OAUTH2_ENDPOINT = "/oauth2";
    public final static String OAUTH2_ACCESS_TOKEN_ENDPOINT = "/oauth2/accessToken";
}
