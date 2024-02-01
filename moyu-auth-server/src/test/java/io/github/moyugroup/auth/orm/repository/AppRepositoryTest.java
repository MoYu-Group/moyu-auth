package io.github.moyugroup.auth.orm.repository;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.orm.model.App;
import io.github.moyugroup.auth.util.KeyGeneratorUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.List;

/**
 * AppRepository 单测
 * <p>
 * Created by fanfan on 2024/02/01.
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
class AppRepositoryTest {

    @Autowired
    AppRepository appRepository;

    @Test
    public void appSaveTest() {
        String appSecret = KeyGeneratorUtil.generatorAppSecret();
        App app = new App();
        app.setAppId("test-app");
        app.setAppName("test-app");
        app.setAppSecret(appSecret);
        app.setAppAesKey("123");
        app.setIsActive(true);

        appRepository.save(app);

        List<App> appList = appRepository.findAll();
        log.info("appList:{}", JSONUtil.toJsonStr(appList));
        Assert.notEmpty(appList, "appList can not be empty!");
    }

}