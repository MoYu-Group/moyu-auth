package io.github.moyugroup.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

/**
 * 登录相关端点
 * <p>
 * Created by fanfan on 2023/08/02.
 */
@Controller
public class LoginController {

    /**
     * 登录页
     *
     * @return
     */
    @GetMapping("/ssoLogin.html")
    public String login(Model model, HttpServletRequest request) {
        // 登录异常处理
        String loginErrorMessage = getLoginErrorMessage(request);
        model.addAttribute("errorMessage", loginErrorMessage);
        return "ssoLogin";
    }

    /**
     * 获取登录错误信息
     *
     * @param request
     * @return
     */
    private String getLoginErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            Object exception = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception instanceof AuthenticationException authenticationException)
                return authenticationException.getMessage();
        }
        return null;
    }
}
