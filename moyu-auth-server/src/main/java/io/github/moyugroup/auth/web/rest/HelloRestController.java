package io.github.moyugroup.auth.web.rest;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.orm.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Object user() {
        User user = new User();
        user.setCreator("sys");
        user.setModifier("sys");
        user.setUserId("user001");
        user.setUsername("noisky1");
        user.setPassword("passss");
        User save = userRepository.saveAndFlush(user);
        log.info("save user:{}", JSONUtil.toJsonStr(save));
        User referenceById = userRepository.getReferenceById(save.getId());
        log.info("referenceById user:{}", JSONUtil.toJsonStr(referenceById));
        referenceById.setEmail("1231");
        userRepository.saveAndFlush(referenceById);
        User referenceById1 = userRepository.getReferenceById(save.getId());
        return referenceById1;
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
