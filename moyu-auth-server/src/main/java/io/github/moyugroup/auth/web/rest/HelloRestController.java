package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.service.LoginCacheService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fanfan on 2022/05/12.
 */
@RestController
public class HelloRestController {

    @Resource
    private LoginCacheService loginCacheService;

    @GetMapping("hello")
    public String helleWorld() {
        return "hello Moyu-Auth-Server !!!";
    }

    @PostMapping("postHello")
    public String postHello() {
        return "hello postHello !!!";
    }

}
