package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.pojo.request.UserSaveRequest;
import io.github.moyugroup.auth.service.UserService;
import io.github.moyugroup.base.model.pojo.Result;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("open/user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    @Resource
    UserService userService;

    /**
     * 用户新增
     *
     * @param userSaveRequest
     * @return
     */
    @PostMapping("save")
    public Result<Boolean> userSave(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        return Result.success(userService.userSave(userSaveRequest));
    }
}
