package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.convert.UserConvert;
import io.github.moyugroup.auth.manage.UserManage;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.pojo.vo.UserVO;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.util.AssertUtil;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SSO 登录服务
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SSOLoginService {

    @Resource
    UserManage userManage;

    /**
     * 用户账户登录
     *
     * @param username
     * @param password
     */
    public UserVO userLoginByAccount(String username, String password) {
        // 查询用户是否存在
        User user = userManage.getUserByUsername(username);
        AssertUtil.notNull(user, ErrorCodeEnum.WRONG_USER_ACCOUNT_OR_PASSWORD);

        // 校验密码是否正确 todo 使用 Bcrypt 加密
        AssertUtil.isTrue(password.equals(user.getPassword()), ErrorCodeEnum.WRONG_USER_ACCOUNT_OR_PASSWORD);

        // todo 校验用户登录状态

        // todo 更新用户最后登录时间

        return UserConvert.INSTANCE.userToUserVO(user);
    }

}
