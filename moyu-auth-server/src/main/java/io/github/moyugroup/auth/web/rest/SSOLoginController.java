package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.model.request.SSOLoginRequest;
import io.github.moyugroup.auth.model.request.SwitchTenantRequest;
import io.github.moyugroup.auth.model.vo.AppVO;
import io.github.moyugroup.auth.model.vo.RedirectUrlVO;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        // 登录成功后返回重定向地址
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
        // 切换租户成功后返回重定向地址
        String redirectUrl = SSOLoginConstant.LOGIN_PAGE_PATH + UrlUtil.getRedirectParam(request);
        return Result.success(new RedirectUrlVO().setRedirectUrl(redirectUrl));
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(SSOLoginConstant.LOGOUT_ENDPOINT)
    public void ssoLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 参数准备
        String appId = MoYuLoginUtil.getRequestParamAppId(request);
        String backUrl = request.getParameter(SSOLoginConstant.BACK_URL);
        AppVO appVO = appService.getAppById(appId);
        MoYuLoginUtil.checkAppIsOk(appVO);

        // MoYu 登录中心退出登录
        ssoLoginService.userLogout(request, response);

        // 登录完成后重定向
        if (StringUtils.isNoneBlank(backUrl)) {
            // 重定向回应用 backUrl
            response.sendRedirect(backUrl);
        } else {
            // 没有携带 backUrl，则重定向回应用配置回调地址
            response.sendRedirect(appVO.getRedirectUri());
        }
    }

}
