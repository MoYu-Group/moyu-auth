package io.github.moyugroup.auth.web.page;

import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.pojo.vo.SwitchTenantVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.util.MoYuLoginUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

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
     * 切换登录页面渲染
     *
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/switchTenant.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String switchTenant(Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        List<SwitchTenantVO> switchTenantVOS = Arrays.asList(
                new SwitchTenantVO("1", "摸鱼开发组"),
                new SwitchTenantVO("2", "机械工程组织交互"),
                new SwitchTenantVO("3", "农夫圈区"),
                new SwitchTenantVO("4", "觅品科技"),
                new SwitchTenantVO("5", "蜂巢数据中心")
                // 可以根据需要添加更多租户
        );
        model.addAttribute("tenantList", switchTenantVOS);
        return "switchTenant";
    }

    /**
     * 登录应用前置检查，登录时会再次检查
     *
     * @param request
     */
    private void checkAppInfo(HttpServletRequest request) {
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

    private void fillPageHideParam(Model model, HttpServletRequest request) {
        String loginErrorMessage = MoYuLoginUtil.getLoginErrorMessage(request);
        model.addAttribute("errorMessage", loginErrorMessage);
        model.addAttribute(MoYuOAuthConstant.APP_ID_PARAM, request.getParameter(MoYuOAuthConstant.APP_ID_PARAM));
        model.addAttribute(MoYuOAuthConstant.BACK_URL_PARAM, request.getParameter(MoYuOAuthConstant.BACK_URL_PARAM));
    }

}
