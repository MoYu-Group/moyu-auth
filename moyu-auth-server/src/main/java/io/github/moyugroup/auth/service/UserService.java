package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.constant.enums.UserStatusEnum;
import io.github.moyugroup.auth.convert.UserConvert;
import io.github.moyugroup.auth.manage.UserManage;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.pojo.request.UserSaveRequest;
import io.github.moyugroup.auth.util.UserIdGeneratorUtil;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户服务
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    @Resource
    UserManage userManage;

    /**
     * 用户新增
     *
     * @param userSaveRequest
     * @return
     */
    public boolean userSave(UserSaveRequest userSaveRequest) {
        User user = UserConvert.INSTANCE.userSaveRequestToUser(userSaveRequest);
        // todo 密码使用 Bcrypt 加密
        user.setUserId(UserIdGeneratorUtil.getNextUserId());
        user.setUserStatus(UserStatusEnum.INACTIVE);
        User saveUser = userManage.userSave(user);
        return Objects.nonNull(saveUser);
    }
}
