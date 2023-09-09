package io.github.moyugroup.auth.handler;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.constant.MoYuAuthLoginConstant;
import io.github.moyugroup.auth.pojo.bo.UserLoginAppBO;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.pojo.vo.OAuth2UserVO;
import io.github.moyugroup.auth.pojo.vo.UserVO;
import io.github.moyugroup.auth.service.LoginCacheService;
import io.github.moyugroup.auth.util.LoginUtil;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * MoYu-Auth 登录中心，登录成功后的统一处理
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
public class MoYuAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private LoginCacheService loginCacheService;

    public MoYuAuthSuccessHandler(LoginCacheService loginCacheService) {
        this.loginCacheService = loginCacheService;
    }

    /**
     * 登录成功后的处理
     *
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("MoYuAuthSuccessHandler with authentication:{}", JSONUtil.toJsonStr(authentication));
        if (Objects.isNull(authentication)) {
            log.error("用户登录信息 Authentication 不存在");
            throw new BizException(ErrorCodeEnum.USER_LOGIN_EXCEPTION);
        }
        // 获取登录应用信息
        AppVO appVO = getAppVO(request);
        // 一方应用，不进行 OAuth 登录
        if (LoginUtil.checkIsMoYuAuthApp(appVO.getAppId())) {
            handle(request, response, authentication);
        }
        // 二方应用，进行 OAuth 登录流程
        // 获取用户信息
        OAuth2UserVO oAuthUserVO = getOAuthUserVO(authentication);
        // 生成 ssoToken
        String ssoToken = IdUtil.fastSimpleUUID();
        // 储存 ssoToken 的登录信息
        loginCacheService.saveUserLoginToken(ssoToken, oAuthUserVO);
        // 保存用户登录应用信息
        loginCacheService.saveUserLoginApp(oAuthUserVO.getUserId(), buildUserLoginAppBO(appVO, ssoToken));
        // 获取登录成功后回调地址
        String callBackUrl = getSsoCallBackUrl(appVO, ssoToken, getBackUrl(request));
        log.info("OAuth2 SSO sendRedirect:{}", callBackUrl);
        redirectStrategy.sendRedirect(request, response, callBackUrl);
        clearSessionAttributes(request);
    }

    private String getBackUrl(HttpServletRequest request) {
        return request.getParameter(MoYuAuthLoginConstant.BACK_URL_PARAM);
    }

    /**
     * 构建用户登录的应用对象
     *
     * @param appVO
     * @param ssoToken
     * @return
     */
    private UserLoginAppBO buildUserLoginAppBO(AppVO appVO, String ssoToken) {
        UserLoginAppBO userLoginAppBO = new UserLoginAppBO();
        userLoginAppBO.setAppId(appVO.getAppId());
        userLoginAppBO.setAppUrl(appVO.getAppUrl());
        userLoginAppBO.setLogoutCallbackUrl(appVO.getSsoCallbackPath());
        userLoginAppBO.setSsoToken(ssoToken);
        return userLoginAppBO;
    }

    private OAuth2UserVO getOAuthUserVO(Authentication authentication) {
        UserVO userVO = (UserVO) authentication.getPrincipal();
        OAuth2UserVO oAuth2UserVO = new OAuth2UserVO();
        BeanUtils.copyProperties(userVO, oAuth2UserVO);
        return oAuth2UserVO;
    }

    /**
     * 获取登录成功后回调地址，并添加回调
     *
     * @param appVO
     * @param ssoToken
     * @param backUrl
     * @return
     */
    private String getSsoCallBackUrl(AppVO appVO, String ssoToken, String backUrl) {
        UrlBuilder callBackUrlBuilder = UrlBuilder.of(appVO.getAppUrl());
        String ssoCallBackUrl = appVO.getSsoCallbackPath();
        if (StringUtils.isBlank(ssoCallBackUrl)) {
            // 未指定回调路径，使用默认回调地址
            callBackUrlBuilder.setPath(UrlPath.of(MoYuAuthLoginConstant.SSO_CALLBACK_PATH, Charset.defaultCharset()));
        } else {
            // 指定路径回调
            callBackUrlBuilder.setPath(UrlPath.of(ssoCallBackUrl, Charset.defaultCharset()));
        }

        UrlQuery urlQuery = new UrlQuery();
        // 添加 ssoToken 参数
        urlQuery.add(MoYuAuthLoginConstant.SSO_TOKEN_PARAM, ssoToken);
        if (StringUtils.isNotBlank(backUrl)) {
            // 添加 backUrl 参数
            urlQuery.add(MoYuAuthLoginConstant.BACK_URL_PARAM, URLEncoder.encode(backUrl, StandardCharsets.UTF_8));
        }

        // 构建sso回调地址并返回
        return callBackUrlBuilder + "?" + urlQuery;
    }


    private AppVO getAppVO(HttpServletRequest request) {
        Object appInfoObj = request.getAttribute(MoYuAuthLoginConstant.REQUEST_APP_INFO);
        if (Objects.isNull(appInfoObj)) {
            throw new OAuth2AuthenticationException("App Request Cache do not exists");
        }
        return (AppVO) appInfoObj;
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
