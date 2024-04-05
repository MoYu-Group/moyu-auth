package io.github.moyugroup.auth.demo.controller;

import io.github.moyugroup.auth.demo.config.SSOClientProperties;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fanfan on 2023/07/01.
 */
@RestController
public class HelloController {

    @Resource
    private SSOClientProperties SSOClientProperties;

    @GetMapping("hello")
    public String helleWorld() {
        String appId = SSOClientProperties.getAppId();
        return "hello Moyu-Auth-Client !!!";
    }

}
