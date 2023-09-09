package io.github.moyugroup.auth.web.page;

import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.handler.MoYuServerAuthSuccessHandler;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.util.LoginUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Objects;

/**
 * 登录相关端点
 * <p>
 * Created by fanfan on 2023/08/02.
 */
@Slf4j
@Controller
public class LoginPageController {

    @Resource
    private AppService appService;

    @Resource
    private MoYuServerAuthSuccessHandler moYuServerAuthSuccessHandler;

    /**
     * 登录页渲染
     *
     * @return
     */
    @RequestMapping(value = "/ssoLogin.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model, HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws ServletException, IOException {
        checkAppInfo(request);
        if (Objects.nonNull(authentication) && StringUtils.isBlank(LoginUtil.getLoginErrorMessage(request))) {
            log.info("用户：{} 已登录，直接走免登流程", authentication.getName());
            moYuServerAuthSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            return null;
        }
        fillPageHideParam(model, request);
        return "ssoLogin";
    }

    /**
     * 登录应用前置检查，登录时会再次检查
     *
     * @param request
     */
    private void checkAppInfo(HttpServletRequest request) {
        // 应用未传应用ID，则认为是在登录一方应用，设置为系统默认的 appId
        String appId = LoginUtil.getRequestAppId(request);
        // 检查登录 APP 是否存在
        AppVO appById = appService.getAppById(appId);
        try {
            LoginUtil.checkAppIsOk(appById);
        } catch (Exception ex) {
            LoginUtil.setLoginErrorMessage(request, ex.getMessage());
        }
        // 保存登录的应用信息，用于SSO免登成功后获取用户登录的应用使用
        request.setAttribute(MoYuOAuthConstant.REQUEST_APP_INFO, appById);
    }

    private void fillPageHideParam(Model model, HttpServletRequest request) {
        String loginErrorMessage = LoginUtil.getLoginErrorMessage(request);
        model.addAttribute("errorMessage", loginErrorMessage);
        model.addAttribute(MoYuOAuthConstant.APP_ID_PARAM, request.getParameter(MoYuOAuthConstant.APP_ID_PARAM));
        model.addAttribute(MoYuOAuthConstant.BACK_URL_PARAM, request.getParameter(MoYuOAuthConstant.BACK_URL_PARAM));
    }

}
