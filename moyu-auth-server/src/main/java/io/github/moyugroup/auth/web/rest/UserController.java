package io.github.moyugroup.auth.web.rest;

import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.model.converter.UserMapper;
import io.github.moyugroup.auth.model.vo.LoginUserVO;
import io.github.moyugroup.auth.service.UserService;
import io.github.moyugroup.base.model.pojo.Result;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户操作
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("endpoint/user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    @Resource
    UserService userService;

    /**
     * 用户新增
     *
     * @return
     */
    @GetMapping("loginUser")
    public Result<LoginUserVO> loginUser() {
        UserInfo userInfo = UserContext.get();
        LoginUserVO loginUserVO = UserMapper.INSTANCE.toLoginUserVO(userInfo);
        return Result.success(loginUserVO);
    }
}
