package io.github.moyugroup.auth.web.rest;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.constant.enums.UserStatusEnum;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.orm.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fanfan on 2022/05/12.
 */
@Slf4j
@RestController
public class HelloRestController {
    @Resource
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("user")
    public Object user() {
        User user = new User();
        user.setCreator("sys");
        user.setModifier("sys");
        user.setUserId("user001");
        user.setUsername("noisky1");
        user.setPassword("passss");
        user.setUserStatus(UserStatusEnum.ACTIVE);
        User save = userRepository.saveAndFlush(user);
        log.info("save user:{}", JSONUtil.toJsonStr(save));
        return save;
    }

    @GetMapping("hello")
    public String helleWorld() {
        return "hello Moyu-Auth-Server !!!";
    }

    @PostMapping("postHello")
    public String postHello() {
        return "hello postHello !!!";
    }

}
