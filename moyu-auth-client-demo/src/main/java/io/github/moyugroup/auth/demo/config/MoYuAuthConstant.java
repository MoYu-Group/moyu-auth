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

    public final static String APP_ID = "APP_ID";
    public final static String APP_SECRET = "APP_SECRET";
    public final static String LOGIN_IN_URL = "/ssoLogin.html";
    public final static String LOGIN_OUT_URL = "/ssoLogout.html";
    public final static String OAUTH_ENDPOINT = "/oauth2";
    public final static String BACK_URL = "BACK_URL";
    public final static String SSO_TOKEN_PARAM = "SSO_TOKEN";
}
