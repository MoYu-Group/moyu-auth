package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.request.SSOLoginRequest;
import io.github.moyugroup.auth.pojo.request.SwitchTenantRequest;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.service.SSOLoginService;
import io.github.moyugroup.auth.util.MoYuLoginUtil;
import io.github.moyugroup.auth.util.UrlUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

/**
 * 登录接口
 * <p>
 * Created by fanfan on 2024/03/07.
 */
@Slf4j
@Validated
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SSOLoginController {

    @Resource
    SSOLoginService ssoLoginService;

    @Resource
    AppService appService;

    /**
     * SSO 登录接口
     * todo 处理接口请求返回json
     *
     * @param param    登录参数
     * @param request  请求对象
     * @param response 响应对象
     * @throws IOException
     */
    @PostMapping(MoYuOAuthConstant.LOGIN_ENDPOINT)
    public void ssoLogin(@Valid SSOLoginRequest param, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String appId = MoYuLoginUtil.getRequestAppId(request);
        AppVO appVO = appService.getAppById(appId);
        // 登录应用检查
        MoYuLoginUtil.checkAppIsOk(appVO);
        // 用户账密登录
        ssoLoginService.userLoginByAccount(param.getUsername(), param.getPassword(), response);
        // 登录成功后重定向到切换租户页面
        response.sendRedirect(SSOLoginConstant.SWITCH_TENANT_PATH + UrlUtil.getRedirectParam(request));
    }

    /**
     * 切换租户接口
     *
     * @param request
     * @param response
     */
    @PostMapping(MoYuOAuthConstant.SWITCH_TENANT_ENDPOINT)
    public void switchTenant(@Valid SwitchTenantRequest switchTenantRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ssoLoginService.userSwitchTenant(switchTenantRequest.getTenantId(), request);
        // todo 切换租户后重定向逻辑
        String redirectUrl = MoYuOAuthConstant.INDEX_PAGE_PATH;
        response.sendRedirect(redirectUrl);
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

}
