package io.github.moyugroup.auth.controller;

import io.github.moyugroup.auth.constant.MoYuAuthConstant;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.util.LoginUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

/**
 * 登录相关端点
 * <p>
 * Created by fanfan on 2023/08/02.
 */
@Controller
public class LoginController {

    @Resource
    private AppService appService;

    /**
     * 登录页渲染
     *
     * @return
     */
    @RequestMapping(value = "/ssoLogin.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model, HttpServletRequest request) {
//        checkAppId(request);
        fillPageHideParam(model, request);
        return "ssoLogin";
    }

    /**
     * 检查 AppId
     *
     * @param request
     */
    private void checkAppId(HttpServletRequest request) {
        String appId = request.getParameter(MoYuAuthConstant.APP_ID);
        if (StringUtils.isBlank(appId)) {
            LoginUtil.setLoginErrorMessage(request, "appId不能为空");
            return;
        }
        AppVO appById = appService.getAppById(appId);
        if (Objects.isNull(appById)) {
            LoginUtil.setLoginErrorMessage(request, "应用未在统一登录中心注册");
        }
    }

    private void fillPageHideParam(Model model, HttpServletRequest request) {
        String loginErrorMessage = LoginUtil.getLoginErrorMessage(request);
        model.addAttribute("errorMessage", loginErrorMessage);
        model.addAttribute(MoYuAuthConstant.APP_ID, request.getParameter(MoYuAuthConstant.APP_ID));
        model.addAttribute(MoYuAuthConstant.BACK_URL, request.getParameter(MoYuAuthConstant.BACK_URL));
    }

}
