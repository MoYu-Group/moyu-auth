package io.github.moyugroup.auth.constant;

/**
 * Moyu-Auth 常量类
 * <p>
 * Created by fanfan on 2023/09/03.
 */
public class MoYuOAuthConstant {
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
     * MoYu-Auth 主页路径
     */
    public final static String INDEX_PAGE_PATH = "/";
    /**
     * MoYu-Auth 默认登录页面路径
     */
    public final static String LOGIN_PAGE_PATH = "/ssoLogin.html";
    /**
     * MoYu-Auth 默认注销页面路径
     */
    public final static String LOGIN_OUT_URL = "/ssoLogout.html";
    /**
     * MoYu-Auth 系统默认登录端点
     */
    public final static String LOGIN_ENDPOINT = "/ssoLogin";
    /**
     * MoYu-Auth 系统默认注销端点
     */
    public final static String LOGIN_OUT_ENDPOINT = "/ssoLogout";
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
    public final static String MOYU_AUTH = "moyu-auth";
    /**
     * App 信息在 Request 中的属性名称
     */
    public final static String REQUEST_APP_INFO = "REQUEST_APP_INFO";
    /**
     * OAuth2 登陆回调路径
     */
    public final static String SSO_CALLBACK_PATH = "oauth2";
    /**
     * OAuth2 登录缓存类型配置名称
     */
    public final static String MOYU_AUTH_CACHE_FIELD_NAME = "moyu.auth.server.cache";

    /**
     * 启用内存缓存
     */
    public final static String MOYU_AUTH_CACHE_MEM = "memory";
}
