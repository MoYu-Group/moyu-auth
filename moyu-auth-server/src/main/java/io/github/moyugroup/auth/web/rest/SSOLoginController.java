package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.pojo.request.SSOLoginRequest;
import io.github.moyugroup.auth.service.SSOLoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 * <p>
 * Created by fanfan on 2024/03/07.
 */
@Slf4j
@Validated
@RestController
public class SSOLoginController {

    @Resource
    private SSOLoginService ssoLoginService;

    @PostMapping("ssoLogin")
    public void ssoLogin(@Valid SSOLoginRequest ssoLoginRequest, HttpServletResponse response) {
        ssoLoginService.userLoginByAccount(ssoLoginRequest.getUsername(), ssoLoginRequest.getPassword(), response);
    }

}
