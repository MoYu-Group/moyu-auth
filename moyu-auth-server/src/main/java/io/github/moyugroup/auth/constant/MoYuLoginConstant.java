package io.github.moyugroup.auth.constant;

/**
 * Moyu 登录常量类
 * <p>
 * Created by fanfan on 2023/09/03.
 */
public class MoYuLoginConstant {
    /**
     * 登录过期时间，24小时
     */
    public static final int LOGIN_EXPIRE_SECONDS = 24 * 60 * 60;
    /**
     * 登录 Cookie Key
     */
    public static final String LOGIN_COOKIE_KEY = MoYuOAuthConstant.MOYU_AUTH + "_SSO_TOKEN_V1";
}
