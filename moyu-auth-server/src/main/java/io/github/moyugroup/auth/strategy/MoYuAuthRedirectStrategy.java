package io.github.moyugroup.auth.strategy;

import cn.hutool.core.net.url.UrlQuery;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.DefaultRedirectStrategy;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * MoYu-Auth 重定向策略
 * <p>
 * Created by fanfan on 2023/09/04.
 */
@Slf4j
public class MoYuAuthRedirectStrategy extends DefaultRedirectStrategy {

    /**
     * 一般是登录时发生错误时，重定向到登录页面
     * 这里重写是为了重定向时带上地址栏的参数
     *
     * @param request
     * @param response
     * @param url
     */
    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        url = checkAndAddParam(request, url);
        super.sendRedirect(request, response, url);
    }

    /**
     * 检查和添加重定向参数
     *
     * @param request
     * @param url
     * @return
     */
    private String checkAndAddParam(HttpServletRequest request, String url) {
        UrlQuery urlQuery = new UrlQuery();
        String appId = request.getParameter(MoYuOAuthConstant.APP_ID_PARAM);
        if (StringUtils.isNotBlank(appId)) {
            urlQuery.add(MoYuOAuthConstant.APP_ID_PARAM, appId);
        }
        String backUrl = request.getParameter(MoYuOAuthConstant.BACK_URL_PARAM);
        if (StringUtils.isNotBlank(backUrl)) {
            urlQuery.add(MoYuOAuthConstant.BACK_URL_PARAM, URLEncoder.encode(backUrl, StandardCharsets.UTF_8));
        }
        return url + "?" + urlQuery;
    }

}
