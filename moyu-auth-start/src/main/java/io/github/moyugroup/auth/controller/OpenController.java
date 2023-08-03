package io.github.moyugroup.auth.controller;

import io.github.moyugroup.base.model.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 免登录接口
 * <p>
 * Created by fanfan on 2023/07/01.
 */
@RestController
public class OpenController {
    @GetMapping("open/test")
    public Result<?> helleWorld() {
        return Result.success("open test");
    }
}
