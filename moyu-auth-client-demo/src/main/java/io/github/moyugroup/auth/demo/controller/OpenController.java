package io.github.moyugroup.auth.demo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.util.AssertUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 免登录接口
 * <p>
 * Created by fanfan on 2023/07/01.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("open")
public class OpenController {

    @Resource
    private SessionRegistry sessionRegistry;


    @GetMapping("test")
    public Result<?> helleWorld() {
        return Result.success("open test");
    }

    @GetMapping("buildLogin")
    public Result<?> buildLogin() {

        return Result.success();
    }

    @GetMapping("session")
    public Result<?> session() {
        List<Object> result = new ArrayList<>();
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        result.add(StrUtil.format("online user {}", allPrincipals.size()));
        for (Object allPrincipal : allPrincipals) {
            List<SessionInformation> allSessions = sessionRegistry.getAllSessions(allPrincipal, true);
            for (SessionInformation allSession : allSessions) {
                User principal = (User) allSession.getPrincipal();
                JSONObject json = new JSONObject();
                json.set("sessionId", allSession.getSessionId());
                json.set("userName", principal.getUsername());
                json.set("lastLogin", DateTime.of(allSession.getLastRequest()).toString());
                result.add(json);
            }

        }
        return Result.success(result);
    }

    @GetMapping("expireNow")
    public Result<?> expireNow(@Valid @NotBlank String id) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(id);
        AssertUtil.notNull(sessionInformation, "session不存在");
        sessionInformation.expireNow();
        return Result.success(StrUtil.format("sessionId:{} 已经退出登录"));
    }
}
