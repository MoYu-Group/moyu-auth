package io.github.moyugroup.auth.controller.rest;

import io.github.moyugroup.auth.pojo.request.AccessTokenRequest;
import io.github.moyugroup.base.model.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Oauth2 接口
 * <p>
 * Created by fanfan on 2023/09/05.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("oauth2")
public class Oauth2RestController {

    @PostMapping("accessToken")
    public Result<?> accessToken(@RequestBody AccessTokenRequest accessTokenRequest) {
        return Result.success("get open test");
    }

}
