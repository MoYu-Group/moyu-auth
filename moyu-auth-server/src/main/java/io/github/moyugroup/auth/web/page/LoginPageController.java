package io.github.moyugroup.auth.web.page;

import io.github.moyugroup.auth.common.context.UserContext;
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

import java.util.List;

/**
 * 登录相关页面渲染
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
     * 如果已经登录，则重定向到应用或者
     *
     * @param model    页面模型
     * @param request  请求对象
     * @param response 响应对象
     * @return 页面模版名称
     */
    @RequestMapping(value = "/ssoLogin.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String ssoLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
        String appId = MoYuLoginUtil.getRequestAppId(request);
        AppVO appById = appService.getAppById(appId);
        boolean allowLogin = true;
        String errorMsg = "";
        try {
            MoYuLoginUtil.checkAppIsOk(appById);
        } catch (Exception ex) {
            // APP验证失败，禁止继续登录，返回错误信息
            allowLogin = false;
            errorMsg = ex.getMessage();
        }
        MoYuLoginUtil.fillPageParam(model, allowLogin, errorMsg);
        return "ssoLogin";
    }

    /**
     * 切换租户页面渲染
     *
     * @param model    页面模型
     * @param request  请求对象
     * @param response 响应对象
     * @return 页面模版名称
     */
    @RequestMapping(value = "/switchTenant.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String switchTenant(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<SwitchTenantVO> switchTenantVOS = tenantUserService.getSwitchTenantVOsByUserId(UserContext.getCurUserId(),
                MoYuLoginUtil.getRequestAppId(request));
        model.addAttribute("tenantList", switchTenantVOS);
        return "switchTenant";
    }

}
