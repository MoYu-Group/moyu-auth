package io.github.moyugroup.auth.handler;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 摸鱼登录中心，登录成功后的统一处理
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
public class MoYuAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    /**
     * Calls the parent class {@code handle()} method to forward or redirect to the target
     * URL, and then calls {@code clearAuthenticationAttributes()} to remove any leftover
     * session data.
     *
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("MoYuAuthSuccessHandler authentication:{}", JSONUtil.toJsonStr(authentication));
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
