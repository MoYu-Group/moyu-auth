package io.github.moyugroup.auth.demo.controller;

import io.github.moyugroup.auth.demo.config.MoYuAuthClientProperties;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fanfan on 2023/07/01.
 */
@RestController
public class HelloController {

    @Resource
    private MoYuAuthClientProperties moYuAuthClientProperties;

    @GetMapping("hello")
    public String helleWorld() {
        String appId = moYuAuthClientProperties.getAppId();
        return "hello Moyu-Auth-Client !!!";
    }

}
