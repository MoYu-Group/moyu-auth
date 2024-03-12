package io.github.moyugroup.auth.util;

import io.github.moyugroup.auth.constant.MoYuLoginConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Cookie 工具类
 * <p>
 * Created by fanfan on 2024/03/07.
 */
@Slf4j
public class CookieUtil {

    /**
     * 写入登录 Cookie
     *
     * @param sessionId
     */
    public static void writeLoginCookie(String sessionId, HttpServletResponse response) {
        // 写入用户 Cookie
        Cookie userCookie = new Cookie(MoYuLoginConstant.LOGIN_COOKIE_KEY, sessionId);
        // 设置Cookie的有效期
        userCookie.setMaxAge(MoYuLoginConstant.LOGIN_EXPIRE_SECONDS);
        // 设置Cookie的路径，这样只有请求该路径的时候，Cookie才会被发送到服务器
        userCookie.setPath("/");
        // todo Cookie 安全性增强
        // 将Cookie添加到响应中
        response.addCookie(userCookie);
    }

}
