package io.github.moyugroup.auth.util;

import io.github.moyugroup.auth.constant.MoYuAuthLoginConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;

import java.util.Objects;

/**
 * 登录工具类
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
public class LoginUtil {

    /**
     * 获取登录错误信息
     *
     * @param request
     * @return
     */
    public static String getLoginErrorMessage(HttpServletRequest request) {
        Object errorAttribute = request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (Objects.nonNull(errorAttribute)) {
            if (errorAttribute instanceof AuthenticationException authenticationException) {
                request.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                return authenticationException.getMessage();
            }
        }
        HttpSession session = request.getSession(false);
        if (Objects.nonNull(session)) {
            Object exception = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception instanceof AuthenticationException authenticationException) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                return authenticationException.getMessage();
            }
            Object loginErrorMsg = session.getAttribute(MoYuAuthLoginConstant.LOGIN_ERROR_MESSAGE);
            if (Objects.nonNull(loginErrorMsg)) {
                session.removeAttribute(MoYuAuthLoginConstant.LOGIN_ERROR_MESSAGE);
                return (String) loginErrorMsg;
            }
        }
        return null;
    }

    /**
     * 设置登录错误信息
     *
     * @param request
     * @param errorMessage
     * @return
     */
    public static void setLoginErrorMessage(HttpServletRequest request, String errorMessage) {
        HttpSession session = request.getSession();
        if (StringUtils.isBlank(errorMessage)) {
            return;
        }
        session.setAttribute(MoYuAuthLoginConstant.LOGIN_ERROR_MESSAGE, errorMessage);
    }
}
