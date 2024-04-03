package io.github.moyugroup.auth.demo.util;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.common.util.AESCipherUtil;
import io.github.moyugroup.auth.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * SSO用户工具类
 * <p>
 * Created by fanfan on 2024/03/30.
 */
@Slf4j
public class SSOLoginUtil {

    /**
     * 从 Cookie 中获取并解析用户信息
     *
     * @param request   request 对象
     * @param appId     appId
     * @param appSecret appSecret
     * @return UserInfo
     */
    public static UserInfo getUserFromCookie(HttpServletRequest request, String appId, String appSecret) {
        // 获取应用 Cookie
        String cookieValue = CookieUtil.getCookieValue(request, appId);
        if (StringUtils.isBlank(cookieValue)) {
            return null;
        }
        // 从 Cookie 中解析用户信息
        String userJsonStr = AESCipherUtil.decryptData(appSecret, cookieValue);
        if (StringUtils.isBlank(userJsonStr)) {
            return null;
        }
        return JSONUtil.toBean(userJsonStr, UserInfo.class);
    }

    /**
     * 获取完整访问地址，包含path中的请求参数
     *
     * @param request request
     * @return 完整访问地址
     */
    public static String getFullUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        if (Objects.nonNull(request.getQueryString())) {
            url.append("?").append(request.getQueryString());
        }
        return url.toString();
    }

    /**
     * 获取请求的 host
     *
     * @param request 请求对象
     * @return host
     */
    public static String getRequestHost(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        return getHostByUrl(url.toString());
    }

    /**
     * 获取网址的 host
     *
     * @param url 网址
     * @return host
     */
    public static String getHostByUrl(String url) {
        UrlBuilder urlBuilder = UrlBuilder.of(url);
        return urlBuilder.getHost();
    }

    /**
     * 通过 ssoToken 获取用户信息，建立登录态
     *
     * @param ssoToken
     * @param request
     * @param response
     */
    public static void handleSSOToken(String ssoToken, HttpServletRequest request, HttpServletResponse response) {

    }
}
