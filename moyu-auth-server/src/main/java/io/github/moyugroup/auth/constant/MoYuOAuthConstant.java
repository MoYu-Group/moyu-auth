package io.github.moyugroup.auth.constant;

/**
 * Moyu-Auth 常量类
 * <p>
 * Created by fanfan on 2023/09/03.
 */
public class MoYuOAuthConstant {
    /**
     * MoYu-Auth 主页路径
     */
    public final static String INDEX_PAGE_PATH = "/";
    /**
     * MoYu-Auth 系统默认切换租户端点
     */
    public final static String SWITCH_TENANT_ENDPOINT = "/api/switch/tenant/doSwitch";
    /**
     * 登录错误信息 属性名称
     */
    public final static String LOGIN_ERROR_MESSAGE = "errorMessage";
    /**
     * 是否允许继续登录
     */
    public final static String ALLOW_LOGIN = "allowLogin";
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
     * OAuth2 登录缓存类型配置名称
     */
    public final static String MOYU_AUTH_CACHE_FIELD_NAME = "moyu.auth.server.cache";

    /**
     * 启用内存缓存
     */
    public final static String MOYU_AUTH_CACHE_MEM = "memory";
}
