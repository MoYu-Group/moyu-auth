package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.constant.enums.OAuth2ErrorEnum;
import io.github.moyugroup.auth.constant.enums.SSOLoginTypeEnum;
import io.github.moyugroup.auth.pojo.request.SSOLoginRequest;
import io.github.moyugroup.auth.pojo.vo.UserVO;
import io.github.moyugroup.auth.service.SSOLoginService;
import io.github.moyugroup.util.AssertUtil;
import jakarta.annotation.Resource;
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
    public void ssoLogin(@Valid SSOLoginRequest ssoLoginRequest) {
        SSOLoginTypeEnum ssoLoginType = SSOLoginTypeEnum.getByValue(ssoLoginRequest.getLoginType());
        AssertUtil.notNull(ssoLoginType, OAuth2ErrorEnum.SSO_LOGIN_TYPE_INVALID);

        UserVO userVO = ssoLoginService.userLoginByAccount(ssoLoginRequest.getUsername(), ssoLoginRequest.getPassword());

        // todo 构建用户中心登录态

        return;
    }

}
