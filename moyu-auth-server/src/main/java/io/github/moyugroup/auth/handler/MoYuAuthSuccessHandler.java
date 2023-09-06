package io.github.moyugroup.auth.handler;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.constant.MoYuAuthLoginConstant;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.pojo.vo.OAuth2UserVO;
import io.github.moyugroup.auth.pojo.vo.UserVO;
import io.github.moyugroup.auth.service.OAuthCacheService;
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

    private OAuthCacheService oAuthCacheService;

    public MoYuAuthSuccessHandler(OAuthCacheService oAuthCacheService) {
        this.oAuthCacheService = oAuthCacheService;
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
        AppVO appVO = getAppVO(request);
        String backUrl = request.getParameter(MoYuAuthLoginConstant.BACK_URL_PARAM);
        // 发放 SSO_TOKEN
        String ssoToken = IdUtil.fastSimpleUUID();
        // 储存 ssoToken 的登录信息
        oAuthCacheService.setTokenWithUser(ssoToken, getOAuthUserVO(authentication));
        // 获取回调地址
        String callBackUrl = getSsoCallBackUrl(appVO, ssoToken, backUrl);
        if (StringUtils.isNotBlank(callBackUrl)) {
            redirectStrategy.sendRedirect(request, response, callBackUrl);
        } else {
            // 回调地址不存在时，走默认跳转逻辑
            handle(request, response, authentication);
        }
        clearSessionAttributes(request);
    }

    private OAuth2UserVO getOAuthUserVO(Authentication authentication) {
        UserVO userVO = (UserVO) authentication.getPrincipal();
        OAuth2UserVO oAuth2UserVO = new OAuth2UserVO();
        BeanUtils.copyProperties(userVO, oAuth2UserVO);
        return oAuth2UserVO;
    }

    /**
     * 获取回调地址，并添加请求参数
     *
     * @param appVO
     * @param ssoToken
     * @param backUrl
     * @return
     */
    private String getSsoCallBackUrl(AppVO appVO, String ssoToken, String backUrl) {
        String ssoCallBackUrl;
        if (StringUtils.isNotBlank(appVO.getSsoCallBackUrl())) {
            // 应用设置了回调地址以应用为准
            ssoCallBackUrl = appVO.getSsoCallBackUrl();
        } else {
            // 应用未设置回调地址以 backUrl 为准
            UrlBuilder of = UrlBuilder.of(backUrl);
            of.setPath(UrlPath.of(MoYuAuthLoginConstant.SSO_CALLBACK_PATH, Charset.defaultCharset()));
            ssoCallBackUrl = of.toString();
        }
        UrlQuery urlQuery = new UrlQuery();
        urlQuery.add(MoYuAuthLoginConstant.SSO_TOKEN_PARAM, ssoToken);
        if (StringUtils.isNotBlank(backUrl)) {
            urlQuery.add(MoYuAuthLoginConstant.BACK_URL_PARAM, URLEncoder.encode(backUrl, StandardCharsets.UTF_8));
        }
        return ssoCallBackUrl + "?" + urlQuery;
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
