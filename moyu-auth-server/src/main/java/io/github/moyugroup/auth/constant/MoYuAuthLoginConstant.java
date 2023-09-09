package io.github.moyugroup.auth.constant;

/**
 * Moyu-Auth 常量类
 * <p>
 * Created by fanfan on 2023/09/03.
 */
public class MoYuAuthLoginConstant {
    /**
     * 应用 AppId 参数名称
     */
    public final static String APP_ID_PARAM = "appId";
    /**
     * 应用 AppSecret 参数名称
     */
    public final static String APP_SECRET_PARAM = "appSecret";
    /**
     * 回调地址参数名称
     */
    public final static String BACK_URL_PARAM = "backUrl";
    /**
     * SSO_TOKEN 参数名称
     */
    public final static String SSO_TOKEN_PARAM = "ssoToken";
    /**
     * SSO 登录地址
     */
    public final static String LOGIN_IN_URL = "/ssoLogin.html";
    /**
     * SSO 退出登录地址
     */
    public final static String LOGIN_OUT_URL = "/ssoLogout.html";
    /**
     * 登录错误信息 属性名称
     */
    public final static String LOGIN_ERROR_MESSAGE = "LOGIN_ERROR_MESSAGE";
    /**
     * 登录用户名 参数名称
     */
    public final static String LOGIN_USERNAME_PARAM = "username";
    /**
     * 登录密码 参数名称
     */
    public final static String LOGIN_PASSWORD_PARAM = "password";
    /**
     * 默认的 AppId
     */
    public final static String DEFAULT_APP_ID = "moyu-auth";
    /**
     * App 信息在 Request 中的属性名称
     */
    public final static String REQUEST_APP_INFO = "REQUEST_APP_INFO";
    /**
     * OAuth2 登陆回调路径
     */
    public final static String SSO_CALLBACK_PATH = "oauth2";

}
