package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.request.SSOLoginRequest;
import io.github.moyugroup.auth.service.SSOLoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
public class SSOLoginController {

    @Resource
    private SSOLoginService ssoLoginService;

    @PostMapping(MoYuOAuthConstant.LOGIN_ENDPOINT)
    public void ssoLogin(@Valid SSOLoginRequest ssoLoginRequest, HttpServletResponse response) throws IOException {
        ssoLoginService.userLoginByAccount(ssoLoginRequest.getUsername(), ssoLoginRequest.getPassword(), response);
        // 重定向到首页
        response.sendRedirect(MoYuOAuthConstant.INDEX_PAGE_PATH);
    }

    @PostMapping(MoYuOAuthConstant.LOGIN_OUT_ENDPOINT)
    public void ssoLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ssoLoginService.userLogout(request, response);
        // 重定向到登录页面
        response.sendRedirect(MoYuOAuthConstant.LOGIN_PAGE_PATH);
    }

}
