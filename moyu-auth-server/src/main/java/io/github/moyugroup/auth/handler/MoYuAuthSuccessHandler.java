package io.github.moyugroup.auth.handler;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 摸鱼登录中心，登录成功后的统一处理
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
public class MoYuAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * 登录成功后的处理
     *
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("MoYuAuthSuccessHandler authentication:{}", JSONUtil.toJsonStr(authentication));
        handle(request, response, authentication);
        clearSessionAttributes(request);
    }

    /**
     * 删除身份验证过程中可能存储在会话中的与身份验证相关的临时数据
     *
     * @param request
     */
    protected final void clearSessionAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
