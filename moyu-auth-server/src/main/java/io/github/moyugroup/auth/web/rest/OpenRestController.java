package io.github.moyugroup.auth.web.rest;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import io.github.moyugroup.auth.pojo.bo.UserLoginAppBO;
import io.github.moyugroup.auth.service.impl.LocalLoginCacheServiceImpl;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.util.AssertUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 免登录接口，现在主要用于开发时查看内部缓存
 * <p>
 * Created by fanfan on 2023/07/01.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("open")
public class OpenRestController {

    @Resource
    private SessionRegistry sessionRegistry;

    @Resource
    private SecurityFilterChain securityFilterChain;

    @GetMapping("test")
    public Result<?> helleWorld() {
        return Result.success("get open test");
    }

    @PostMapping("postTest")
    public Result<?> postOpenTest() {
        return Result.success("post open test");
    }

    @GetMapping("buildLogin")
    public Result<?> buildLogin() {
        List<Filter> filters = securityFilterChain.getFilters();
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
                json.set("expired", allSession.isExpired());
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

    @GetMapping("userLoginApps")
    private Result<?> userLoginApps() {
        Map<Long, Object> map = new HashMap<>();
        TimedCache<Long, Map<String, UserLoginAppBO>> userLoginApp = LocalLoginCacheServiceImpl.userLoginApp;
        Set<Long> keys = userLoginApp.keySet();
        for (Long key : keys) {
            map.put(key, userLoginApp.get(key));
        }
        return Result.success(map);
    }
}
