package io.github.moyugroup.auth.web.rest;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.constant.enums.UserStatusEnum;
import io.github.moyugroup.auth.orm.model.App;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.orm.repository.AppRepository;
import io.github.moyugroup.auth.orm.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by fanfan on 2022/05/12.
 */
@Slf4j
@RestController
public class HelloRestController {
    @Resource
    private UserRepository userRepository;

    @Resource
    private AppRepository appRepository;

    @GetMapping("addUser")
    public Object user(String userId) {
        User user = new User();
        user.setCreator("sys");
        user.setModifier("sys");
        user.setUserId(userId);
        user.setUsername("noisky1");
        user.setPassword("passss");
        user.setMobile("13577778888");
        user.setEmail("abc@abc.com");
        user.setUserStatus(UserStatusEnum.ACTIVE);
        User save = userRepository.saveAndFlush(user);
        log.info("save user:{}", JSONUtil.toJsonStr(save));
        return save;
    }

    @GetMapping("/open/addApp")
    public Object app(String appId) {
        App app = new App();
        app.setAppName(appId);
        app.setAppSecret("1234");
        app.setAppId(appId);
        app.setEnabled(true);
        App save = appRepository.save(app);
        log.info("save save:{}", JSONUtil.toJsonStr(save));
        return save;
    }

    @GetMapping("appList")
    public Object appList() {
        return appRepository.findAll();
    }

    @GetMapping("deleteUser")
    public Object addUser(Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            user.setIsDeleted(true);
            userRepository.save(user);
        }
        return null;
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
