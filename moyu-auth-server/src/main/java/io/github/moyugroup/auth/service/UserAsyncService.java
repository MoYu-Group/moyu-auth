package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.manage.UserManage;
import io.github.moyugroup.auth.orm.model.User;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户异步服务类
 * <p>
 * Created by fanfan on 2024/03/13.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAsyncService {

    @Resource
    UserManage userManage;

    /**
     * 用户登录后操作
     *
     * @param userId
     * @param loginTime
     */
    @Async
    public void afterUserLogin(String userId, LocalDateTime loginTime) {
        log.debug("afterUserLogin userId:{} loginTime:{}", userId, loginTime);
        User user = userManage.getUserByUserId(userId);
        user.setLastLoginTime(loginTime);
        userManage.userSave(user);
    }
}
