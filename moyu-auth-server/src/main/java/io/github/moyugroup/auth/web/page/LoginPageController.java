package io.github.moyugroup.auth.web.page;

import io.github.moyugroup.auth.constant.MoYuAuthLoginConstant;
import io.github.moyugroup.auth.handler.MoYuAuthSuccessHandler;
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
    private MoYuAuthSuccessHandler moYuAuthSuccessHandler;

    /**
     * 登录页渲染
     *
     * @return
     */
    @RequestMapping(value = "/ssoLogin.html", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model, HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws ServletException, IOException {
        checkAppId(request);
        if (Objects.nonNull(authentication) && StringUtils.isBlank(LoginUtil.getLoginErrorMessage(request))) {
            log.info("用户：{} 已登录，直接走免登流程", authentication.getName());
            moYuAuthSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            return null;
        }
        fillPageHideParam(model, request);
        return "ssoLogin";
    }

    /**
     * 检查 AppId
     *
     * @param request
     */
    private void checkAppId(HttpServletRequest request) {
        String appId = request.getParameter(MoYuAuthLoginConstant.APP_ID_PARAM);
        if (StringUtils.isBlank(appId)) {
            LoginUtil.setLoginErrorMessage(request, "appId不能为空");
            return;
        }
        AppVO appById = appService.getAppById(appId);
        if (Objects.isNull(appById)) {
            LoginUtil.setLoginErrorMessage(request, "应用未在统一登录中心注册");
        }
        request.setAttribute(MoYuAuthLoginConstant.REQUEST_APP_INFO, appById);
    }

    private void fillPageHideParam(Model model, HttpServletRequest request) {
        String loginErrorMessage = LoginUtil.getLoginErrorMessage(request);
        model.addAttribute("errorMessage", loginErrorMessage);
        model.addAttribute(MoYuAuthLoginConstant.APP_ID_PARAM, request.getParameter(MoYuAuthLoginConstant.APP_ID_PARAM));
        model.addAttribute(MoYuAuthLoginConstant.BACK_URL_PARAM, request.getParameter(MoYuAuthLoginConstant.BACK_URL_PARAM));
    }

}
