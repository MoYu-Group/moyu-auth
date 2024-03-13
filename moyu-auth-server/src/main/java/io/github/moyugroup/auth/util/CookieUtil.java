package io.github.moyugroup.auth.util;

import io.github.moyugroup.auth.constant.MoYuLoginConstant;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
     * 从请求中获取指定名称的 Cookie 值。
     *
     * @param request 请求对象
     * @param name    Cookie的名称
     * @return 指定Cookie的值，如果没有找到返回null
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        // 获取请求中的所有Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 检查Cookie名称
                if (name.equals(cookie.getName())) {
                    // 返回找到的Cookie的值
                    return cookie.getValue();
                }
            }
        }
        // 如果没有找到指定的Cookie，返回null
        return null;
    }

    /**
     * 获取登录 SessionId
     *
     * @param request
     * @return
     */
    public static String getSSOLoginSessionId(HttpServletRequest request) {
        return getCookieValue(request, MoYuLoginConstant.LOGIN_COOKIE_KEY);
    }

    /**
     * 写入登录 Cookie
     *
     * @param sessionId
     */
    public static void writeSSOLoginCookie(String sessionId, HttpServletResponse response) {
        // 写入用户 Cookie
        Cookie userCookie = new Cookie(MoYuLoginConstant.LOGIN_COOKIE_KEY, sessionId);
        // 设置Cookie的有效期
        userCookie.setMaxAge(MoYuLoginConstant.LOGIN_EXPIRE_SECONDS);
        // 设置Cookie的路径，这样只有请求该路径的时候，Cookie才会被发送到服务器
        userCookie.setPath(MoYuOAuthConstant.INDEX_PAGE_PATH);
        // todo Cookie 安全性增强
        // 将Cookie添加到响应中
        response.addCookie(userCookie);
    }

    /**
     * 删除登录 Cookie
     *
     * @param response
     */
    public static void removeSSOLoginCookie(HttpServletResponse response) {
        Cookie userCookie = new Cookie(MoYuLoginConstant.LOGIN_COOKIE_KEY, null);
        userCookie.setMaxAge(0);
        userCookie.setPath(MoYuOAuthConstant.INDEX_PAGE_PATH);
        response.addCookie(userCookie);
    }
}
