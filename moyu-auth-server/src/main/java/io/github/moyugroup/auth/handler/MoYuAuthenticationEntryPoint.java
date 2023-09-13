package io.github.moyugroup.auth.handler;

import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.web.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.io.IOException;
import java.util.Collections;

/**
 * 匿名用户访问无权限资源时的异常处理
 * <p>
 * Created by fanfan on 2023/09/12.
 */
@Slf4j
public class MoYuAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private MediaTypeRequestMatcher mediaMatcher;

    private LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint;

    public MoYuAuthenticationEntryPoint(String loginFormUrl) {
        this.loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginFormUrl);
        this.mediaMatcher = initRequestMatchers();
    }

    /**
     * 请求匹配器初始化，用于匹配来自浏览器的页面请求
     *
     * @return
     */
    public MediaTypeRequestMatcher initRequestMatchers() {
        MediaTypeRequestMatcher mediaMatcher = new MediaTypeRequestMatcher(new HeaderContentNegotiationStrategy(),
                MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN);
        mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        return mediaMatcher;
    }

    /**
     * 自定义异常处理逻辑
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 是页面请求，跳转到登录页
        if (mediaMatcher.matches(request)) {
            loginUrlAuthenticationEntryPoint.commence(request, response, authException);
            return;
        }
        // 接口请求，返回无权限的 JSON 数据
        WebUtil.writeJsonResponse(Result.fail(ErrorCodeEnum.ACCESS_UNAUTHORIZED));
    }

}
