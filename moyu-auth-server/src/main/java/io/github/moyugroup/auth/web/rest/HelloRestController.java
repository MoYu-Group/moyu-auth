package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.service.LoginCacheService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fanfan on 2022/05/12.
 */
@RestController
public class HelloRestController {

    @Resource
    private LoginCacheService loginCacheService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("hello")
    public String helleWorld() {
//        throw new AccessDeniedException("测试");

        return "hello Moyu-Auth-Server !!!";
    }


}
