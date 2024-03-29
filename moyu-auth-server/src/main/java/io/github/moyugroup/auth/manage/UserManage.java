package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.orm.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * User 表数据操作封装
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserManage {

    @Resource
    UserRepository userRepository;

    /**
     * 根据用户名查询
     *
     * @param userName
     * @return
     */
    public User getUserByUsername(String userName) {
        User user = new User();
        user.setUsername(userName);
        return userRepository.findByUser(user);
    }

    /**
     * 根据用户ID查询
     *
     * @param userId
     * @return
     */
    public User getUserByUserId(String userId) {
        User user = new User();
        user.setUserId(userId);
        return userRepository.findByUser(user);
    }

    /**
     * 用户新增/编辑
     *
     * @param user
     * @return
     */
    public User userSave(User user) {
        return userRepository.save(user);
    }

}
