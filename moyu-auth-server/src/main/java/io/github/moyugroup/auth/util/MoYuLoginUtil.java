package io.github.moyugroup.auth.util;

import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;

import java.util.Objects;

/**
 * 登录工具类
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
public class MoYuLoginUtil {

    /**
     * 获取请求的 AppId
     * 如未传 AppId，则视为一方应用登录，设置为系统默认的 AppId
     *
     * @param request
     * @return
     */
    public static String getRequestAppId(HttpServletRequest request) {
        String appId = request.getParameter(MoYuOAuthConstant.APP_ID_PARAM);
        if (StringUtils.isBlank(appId)) {
            appId = MoYuOAuthConstant.MOYU_AUTH;
        }
        return appId;
    }

    /**
     * 检查登录 APP 信息，如果不符合登录要求则报错
     *
     * @param appVO
     */
    public static void checkAppIsOk(AppVO appVO) {
        // 应用存在判断
        if (Objects.isNull(appVO)) {
            throw new BadCredentialsException("应用未在统一登录中心注册");
        }
        // 二方应用的应用地址必须配置
        if (!MoYuLoginUtil.checkIsMoYuAuthApp(appVO.getAppId()) && StringUtils.isBlank(appVO.getRedirectUri())) {
            throw new BadCredentialsException("应用配置错误，AppUrl 不能为空");
        }
    }

    /**
     * 检查应用是否是 MoYu-Auth 一方应用
     *
     * @param appId
     * @return
     */
    public static boolean checkIsMoYuAuthApp(String appId) {
        return MoYuOAuthConstant.MOYU_AUTH.equals(appId);
    }

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
            Object loginErrorMsg = session.getAttribute(MoYuOAuthConstant.LOGIN_ERROR_MESSAGE);
            if (Objects.nonNull(loginErrorMsg)) {
                session.removeAttribute(MoYuOAuthConstant.LOGIN_ERROR_MESSAGE);
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
        session.setAttribute(MoYuOAuthConstant.LOGIN_ERROR_MESSAGE, errorMessage);
    }
}
