package io.github.moyugroup.auth.util;

import cn.hutool.core.net.url.UrlBuilder;
import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Url 工具类
 * <p>
 * Created by fanfan on 2024/03/30.
 */
@Slf4j
public class UrlUtil {

    /**
     * 获取 request 中的重定向参数
     *
     * @param request
     * @return
     */
    public static String getRedirectParam(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (StringUtils.isBlank(queryString)) {
            return "";
        }
        return "?" + queryString;
    }

    /**
     * 获取应用SSO回调地址
     *
     * @param url
     * @return
     */
    public static String getOAuthSSOTokenHandlerUrl(String url) {
        UrlBuilder urlBuilder = UrlBuilder.of(url);
        urlBuilder.addPath(SSOLoginConstant.SSO_VERIFY_ENDPOINT);
        urlBuilder.setWithEndTag(false);
        return urlBuilder.build();
    }

}
