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
public class MoYuOAuthConstant {

    public final static String APP_ID_PARAM = "appId";
    public final static String APP_SECRET_PARAM = "appSecret";
    public final static String BACK_URL_PARAM = "backUrl";
    public final static String SSO_TOKEN_PARAM = "ssoToken";
    public final static String GRANT_TYPE_PARAM = "grantType";
    public final static String LOGIN_URL = "/ssoLogin.html";
    public final static String LOGOUT_URL = "/ssoLogout.html";
    public final static String LOGOUT_ENDPOINT = "/ssoLogout";
    public final static String OAUTH2_ENDPOINT = "/oauth2";
    public final static String OAUTH2_ACCESS_TOKEN_ENDPOINT = "/oauth2/accessToken";
    /**
     * OAuth2 登录缓存类型配置名称
     */
    public final static String MOYU_AUTH_CACHE = "moyu.auth.server.cache";
    /**
     * 启用本地缓存配置值
     */
    public final static String MOYU_AUTH_CACHE_LOCAL = "local";
    /**
     * Request 对象中储存 ssoToken 的字段名称
     */
    public final static String REQUEST_SSO_TOKEN_FIELD_NAME = "REQUEST_SSO_TOKEN_FIELD_NAME";
}
