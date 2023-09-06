package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.facade.OAuthFacade;
import io.github.moyugroup.auth.pojo.request.AccessTokenRequest;
import io.github.moyugroup.auth.pojo.vo.OAuthUserVO;
import io.github.moyugroup.base.model.pojo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth2 接口
 * <p>
 * Created by fanfan on 2023/09/05.
 */
@Slf4j
@RestController
@RequestMapping("oauth2")
public class Oauth2RestController {

    @Resource
    private OAuthFacade oAuthFacade;

    /**
     * AccessToken 校验
     *
     * @param accessTokenRequest
     * @return
     */
    @PostMapping("accessToken")
    public Result<OAuthUserVO> accessToken(@RequestBody @Validated AccessTokenRequest accessTokenRequest) {
        return Result.success(oAuthFacade.accessToken(accessTokenRequest));
    }

}
