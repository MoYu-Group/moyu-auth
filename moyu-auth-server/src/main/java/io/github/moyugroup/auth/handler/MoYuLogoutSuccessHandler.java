package io.github.moyugroup.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;

/**
 * MoYu-Auth 登录中心，注销成功后的统一处理
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
public class MoYuLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final String logoutSuccessUrl;

    public MoYuLogoutSuccessHandler(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
        super.setDefaultTargetUrl(logoutSuccessUrl);
    }

    /**
     * 注销成功后的逻辑重写
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onLogoutSuccess(request, response, authentication);
    }
}
