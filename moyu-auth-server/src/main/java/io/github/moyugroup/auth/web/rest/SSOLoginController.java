package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.request.SSOLoginRequest;
import io.github.moyugroup.auth.pojo.request.SwitchTenantRequest;
import io.github.moyugroup.auth.service.SSOLoginService;
import io.github.moyugroup.auth.util.MoYuLoginUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

/**
 * 登录接口
 * <p>
 * Created by fanfan on 2024/03/07.
 */
@Slf4j
@Validated
@Controller
public class SSOLoginController {

    @Resource
    private SSOLoginService ssoLoginService;

    /**
     * SSO 登录接口
     *
     * @param ssoLoginRequest
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping(MoYuOAuthConstant.LOGIN_ENDPOINT)
    public void ssoLogin(@Valid SSOLoginRequest ssoLoginRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ssoLoginService.userLoginByAccount(ssoLoginRequest.getUsername(), ssoLoginRequest.getPassword(), response);
        } catch (Exception ex) {
            // 重定向到登录页面并携带错误信息 todo 处理接口请求返回json
            MoYuLoginUtil.setLoginErrorMessage(request, ex.getMessage());
            response.sendRedirect(SSOLoginConstant.LOGIN_PAGE_PATH);
            return;
        }
        // 登录成功后重定向到首页
        response.sendRedirect(MoYuOAuthConstant.INDEX_PAGE_PATH);
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping(MoYuOAuthConstant.LOGIN_OUT_ENDPOINT)
    public void ssoLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ssoLoginService.userLogout(request, response);
        // 重定向到登录页面
        response.sendRedirect(SSOLoginConstant.LOGIN_PAGE_PATH);
    }

    /**
     * 切换租户接口
     *
     * @param request
     * @param response
     */
    @PostMapping(MoYuOAuthConstant.SWITCH_TENANT_ENDPOINT)
    public void switchTenant(@Valid @RequestBody SwitchTenantRequest switchTenantRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ssoLoginService.userSwitchTenant(switchTenantRequest, request);
        String redirectUrl = MoYuOAuthConstant.INDEX_PAGE_PATH;
        if (StringUtils.isNoneBlank(switchTenantRequest.getBackUrl())) {
            // 重定向到 backUrl
            redirectUrl = switchTenantRequest.getBackUrl();
        }
        response.sendRedirect(redirectUrl);
    }

}
