package io.github.moyugroup.auth.orm.repository;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.constant.enums.UserStatusEnum;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.util.UserIdGeneratorUtil;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

/**
 * UserRepositoryTest 单测
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserRepositoryTest {

    @Resource
    UserRepository userRepository;

    User buildTestUser() {
        User user = new User();
        user.setUserId(UserIdGeneratorUtil.getNextUserId());
        user.setUsername("test-user");
        user.setPassword("123123");
        user.setMobile("13333333333");
        user.setEmail("abc@abc.com");
        user.setUserStatus(UserStatusEnum.INACTIVE);
        return user;
    }

    @Test
    void findByUserTest() {
        User user = buildTestUser();
        log.debug("insert user:{}", JSONUtil.toJsonStr(user));
        userRepository.save(user);

        User queryParam = new User();
        queryParam.setUserId(user.getUserId());
        log.debug("queryParam:{}", JSONUtil.toJsonStr(queryParam));
        User queryUser = userRepository.findByUser(queryParam);
        log.debug("queryUser:{}", JSONUtil.toJsonStr(queryUser));

        Assert.notNull(queryUser, "未查询到用户");
    }
}