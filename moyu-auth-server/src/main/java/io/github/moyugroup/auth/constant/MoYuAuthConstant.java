package io.github.moyugroup.auth.constant;

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
    public final static String BACK_URL = "BACK_URL";
    public final static String LOGIN_ERROR_MESSAGE = "LOGIN_ERROR_MESSAGE";
    public final static String LOGIN_USERNAME_PARAMETER = "username";
    public final static String LOGIN_PASSWORD_PARAMETER = "password";
    public final static String DEFAULT_APP_ID = "moyu-server";
}
