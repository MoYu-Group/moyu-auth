package io.github.moyugroup.auth.controller;

import io.github.moyugroup.base.model.pojo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 免登录接口
 * <p>
 * Created by fanfan on 2023/07/01.
 */
@Slf4j
@RestController
@RequestMapping("open")
public class OpenController {

    @Resource
    private SessionRegistry sessionRegistry;

    @GetMapping("test")
    public Result<?> helleWorld() {
        return Result.success("open test");
    }

    @GetMapping("session")
    public Result<?> session() {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        log.info("online user {}", allPrincipals.size());
        return Result.success();
    }

    @GetMapping("expireNow")
    public Result<?> expireNow(String id) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(id);
        sessionInformation.expireNow();
        return Result.success();
    }
}
