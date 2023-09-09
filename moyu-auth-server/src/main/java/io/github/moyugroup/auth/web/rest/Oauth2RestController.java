package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.facade.OAuth2Facade;
import io.github.moyugroup.auth.pojo.request.AccessTokenRequest;
import io.github.moyugroup.auth.pojo.vo.OAuth2UserVO;
import io.github.moyugroup.base.model.pojo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth2 相关接口
 * <p>
 * Created by fanfan on 2023/09/05.
 */
@Slf4j
@RestController
@RequestMapping("oauth2")
public class Oauth2RestController {

    @Resource
    private OAuth2Facade oAuth2Facade;

    /**
     * AccessToken 校验
     * 校验成功返回用户登录信息
     *
     * @param accessTokenRequest
     * @return
     */
    @PostMapping("accessToken")
    public Result<OAuth2UserVO> accessToken(@RequestBody @Validated AccessTokenRequest accessTokenRequest) {
        return Result.success(oAuth2Facade.accessToken(accessTokenRequest));
    }

}
