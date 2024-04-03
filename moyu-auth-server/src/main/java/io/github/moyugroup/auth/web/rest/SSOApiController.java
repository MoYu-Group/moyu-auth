package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.pojo.dto.SSOUserDTO;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.pojo.request.SSOGetUserRequest;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.service.UserTokenService;
import io.github.moyugroup.base.model.pojo.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SSO 登录开放 API
 * <p>
 * Created by fanfan on 2024/03/07.
 */
@Slf4j
@Validated
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SSOApiController {

    @Resource
    AppService appService;

    @Resource
    UserTokenService userTokenService;

    /**
     * 校验 ssoToken，并获取登录的用户信息
     * 该接口基于 OAuth2 授权码模式 + OIDC 协议进行设计
     *
     * @param ssoGetUserRequest
     * @return
     */
    @ResponseBody
    @PostMapping(SSOLoginConstant.OAUTH2_GET_USER_ENDPOINT)
    public Result<SSOUserDTO> ssoGetUser(@Valid @RequestBody SSOGetUserRequest ssoGetUserRequest) {
        // 应用校验
        appService.queryAndCheckApp(ssoGetUserRequest.getAppId(), ssoGetUserRequest.getAppSecret());
        // 根据 ssoToken 获取登录用户
        UserInfo userInfo = userTokenService.getUserBySSOToken(ssoGetUserRequest.getSsoToken());
        // 构建 SSOUserDTO 并返回
        SSOUserDTO ssoUserDTO = new SSOUserDTO()
                .setAppId(ssoGetUserRequest.getAppId())
                .setUser(userInfo);
        return Result.success(ssoUserDTO);
    }

}
