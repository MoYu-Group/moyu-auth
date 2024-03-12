package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.orm.model.UserSession;
import io.github.moyugroup.auth.orm.repository.UserSessionRepository;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserSession 表数据操作封装
 * <p>
 * Created by fanfan on 2024/03/12.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSessionManage {

    @Resource
    UserSessionRepository userSessionRepository;

    /**
     * 用户会话新增
     *
     * @param userSession
     * @return
     */
    public UserSession userSessionSave(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }

}
