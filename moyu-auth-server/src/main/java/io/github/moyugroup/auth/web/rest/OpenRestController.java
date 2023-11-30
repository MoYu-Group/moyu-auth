package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.service.LoginCacheService;
import io.github.moyugroup.base.model.pojo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("test")
    public Result<?> helleWorld() {
        return Result.success("get open test");
    }

    @PostMapping("postTest")
    public Result<?> postOpenTest() {
        return Result.success("post open test");
    }

}
