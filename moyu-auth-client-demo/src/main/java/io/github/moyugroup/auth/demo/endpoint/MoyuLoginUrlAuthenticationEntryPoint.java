package io.github.moyugroup.auth.demo.endpoint;

import cn.hutool.core.net.url.UrlQuery;
import io.github.moyugroup.auth.demo.config.MoYuAuthClientProperties;
import io.github.moyugroup.auth.demo.config.MoyuAuthConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 登录认证端点重写
 * <p>
 * Created by fanfan on 2023/09/03.
 */
public class MoyuLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    static final String DEFAULT_SAVED_REQUEST_ATTR = "SPRING_SECURITY_SAVED_REQUEST";

    private MoYuAuthClientProperties moYuAuthClientProperties;

    /**
     * 构造方法
     *
     * @param loginFormUrl
     * @param moYuAuthClientProperties
     */
    public MoyuLoginUrlAuthenticationEntryPoint(String loginFormUrl, MoYuAuthClientProperties moYuAuthClientProperties) {
        super(loginFormUrl);
        this.moYuAuthClientProperties = moYuAuthClientProperties;
    }

    /**
     * 登录重定向地址重写
     *
     * @param request   the request
     * @param response  the response
     * @param exception the exception
     * @return the URL (cannot be null or empty; defaults to {@link #getLoginFormUrl()})
     */
    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String loginUrl = super.determineUrlToUseForThisRequest(request, response, exception);
        // 构建 URL 参数
        UrlQuery urlQuery = new UrlQuery();
        urlQuery.add(MoyuAuthConstant.APP_ID, moYuAuthClientProperties.getAppId());
        Object attribute = request.getSession().getAttribute(DEFAULT_SAVED_REQUEST_ATTR);
        if (Objects.nonNull(attribute)) {
            DefaultSavedRequest savedRequest = (DefaultSavedRequest) attribute;
            String requestURL = savedRequest.getRequestURL();
            if (StringUtils.isNotBlank(requestURL)) {
                urlQuery.add(MoyuAuthConstant.BACK_URL, URLEncoder.encode(requestURL, StandardCharsets.UTF_8));
            }
        }
        return loginUrl + MoyuAuthConstant.LOGIN_IN_URL + "?" + urlQuery;
    }

}
