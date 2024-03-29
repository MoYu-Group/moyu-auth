package io.github.moyugroup.auth.web.page;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.pojo.vo.SwitchTenantVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.service.TenantUserService;
import io.github.moyugroup.auth.util.MoYuLoginUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 登录相关端点
 * <p>
 * Created by fanfan on 2023/08/02.
 */
@Slf4j
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginPageController {

    @Resource
    AppService appService;

    @Resource
    TenantUserService tenantUserService;

    /**
     * 登录页渲染
     * todo sso登录过程参数的处理
     *
     * @return
     */
    @RequestMapping(value = "/ssoLogin.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        checkAppInfo(request);
        fillPageHideParam(model, request);
        return "ssoLogin";
    }

    /**
     * 切换租户页面渲染
     *
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/switchTenant.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String switchTenant(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<SwitchTenantVO> switchTenantVOS = tenantUserService.getSwitchTenantVOsByUserId(UserContext.getCurUserId());
        model.addAttribute("tenantList", switchTenantVOS);
        return "switchTenant";
    }

    /**
     * 登录应用前置检查，登录时会再次检查
     *
     * @param request
     */
    void checkAppInfo(HttpServletRequest request) {
        // 应用未传应用ID，则认为是在登录一方应用，设置为系统默认的 appId
        String appId = MoYuLoginUtil.getRequestAppId(request);
        // 检查登录 APP 是否存在
        AppVO appById = appService.getAppById(appId);
        try {
            MoYuLoginUtil.checkAppIsOk(appById);
        } catch (Exception ex) {
            MoYuLoginUtil.setLoginErrorMessage(request, ex.getMessage());
        }
        // 保存登录的应用信息，用于SSO免登成功后获取用户登录的应用使用
        request.setAttribute(MoYuOAuthConstant.REQUEST_APP_INFO, appById);
    }

    void fillPageHideParam(Model model, HttpServletRequest request) {
        String loginErrorMessage = MoYuLoginUtil.getLoginErrorMessage(request);
        model.addAttribute("errorMessage", loginErrorMessage);
        model.addAttribute(SSOLoginConstant.APP_ID, request.getParameter(SSOLoginConstant.APP_ID));
        model.addAttribute(SSOLoginConstant.BACK_URL, request.getParameter(SSOLoginConstant.BACK_URL));
    }

}
