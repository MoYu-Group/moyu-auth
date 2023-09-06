package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.service.OAuthCacheService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fanfan on 2022/05/12.
 */
@RestController
public class HelloRestController {

    @Resource
    private OAuthCacheService oAuthCacheService;

    @GetMapping("hello")
    public String helleWorld() {
        oAuthCacheService.getUserByToken("213");
        return "hello Moyu-Auth-Server !!!";
    }


}
