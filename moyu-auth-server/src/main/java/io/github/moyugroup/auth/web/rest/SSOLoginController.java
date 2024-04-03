package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.request.SSOLoginRequest;
import io.github.moyugroup.auth.pojo.request.SwitchTenantRequest;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.pojo.vo.RedirectUrlVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.service.SSOLoginService;
import io.github.moyugroup.auth.util.MoYuLoginUtil;
import io.github.moyugroup.auth.util.UrlUtil;
import io.github.moyugroup.base.model.pojo.Result;
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
import org.springframework.web.bind.annotation.ResponseBody;

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
     *
     * @param param
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @PostMapping(SSOLoginConstant.LOGIN_ENDPOINT)
    public Result<RedirectUrlVO> ssoLogin(@Valid SSOLoginRequest param, HttpServletRequest request, HttpServletResponse response) {
        String appId = MoYuLoginUtil.getRequestParamAppId(request);
        AppVO appVO = appService.getAppById(appId);
        // 登录应用检查
        MoYuLoginUtil.checkAppIsOk(appVO);
        // 用户账密登录
        ssoLoginService.userLoginByAccount(param.getUsername(), param.getPassword(), response);
        // 登录成功后重定向到登录页面
        String redirectUrl = SSOLoginConstant.LOGIN_PAGE_PATH + UrlUtil.getRedirectParam(request);
        return Result.success(new RedirectUrlVO().setRedirectUrl(redirectUrl));
    }

    /**
     * 切换租户接口
     *
     * @param request
     * @param response
     */
    @ResponseBody
    @PostMapping(MoYuOAuthConstant.SWITCH_TENANT_ENDPOINT)
    public Result<RedirectUrlVO> switchTenant(@Valid SwitchTenantRequest switchTenantRequest, HttpServletRequest request, HttpServletResponse response) {
        // 用户切换租户
        ssoLoginService.userSwitchTenant(switchTenantRequest.getTenantId(), request);
        // 登录成功后重定向到登录页面
        String redirectUrl = SSOLoginConstant.LOGIN_PAGE_PATH + UrlUtil.getRedirectParam(request);
        return Result.success(new RedirectUrlVO().setRedirectUrl(redirectUrl));
    }

    /**
     * 退出登录
     * todo 支持sso退出登录，检查AppId并重定向回backUrl
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping(SSOLoginConstant.LOGIN_OUT_ENDPOINT)
    public void ssoLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ssoLoginService.userLogout(request, response);
        // 重定向到登录页面
        response.sendRedirect(SSOLoginConstant.LOGIN_PAGE_PATH);
    }

}
