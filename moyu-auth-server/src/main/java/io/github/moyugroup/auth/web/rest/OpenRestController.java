package io.github.moyugroup.auth.web.rest;

import cn.hutool.json.JSONObject;
import io.github.moyugroup.auth.service.LoginCacheService;
import io.github.moyugroup.auth.service.impl.MemLoginCacheServiceImpl;
import io.github.moyugroup.base.model.pojo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

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
    private LoginCacheService loginCacheService;

    @GetMapping("setCache")
    public Result<?> setCache(String userId, String app, String ssoToken) {
        Map<String, String> map = new HashMap<>();
        map.put(app, ssoToken);
        MemLoginCacheServiceImpl.userLoginApps.put(userId, map);
        return Result.success();
    }

    @GetMapping("cacheStatus")
    public Object setCache() {
        JSONObject json = new JSONObject();
        // 获取缓存的Map视图
        ConcurrentMap<String, Map<String, String>> cacheMap = MemLoginCacheServiceImpl.userLoginApps.asMap();
        // 遍历并打印所有缓存项
        for (Map.Entry<String, Map<String, String>> entry : cacheMap.entrySet()) {
            json.set(entry.getKey(), entry.getValue());
        }
        return json;
    }

    @GetMapping("test")
    public Result<?> helleWorld() {
        return Result.success("get open test");
    }

    @PostMapping("postTest")
    public Result<?> postOpenTest() {
        return Result.success("post open test");
    }

}
