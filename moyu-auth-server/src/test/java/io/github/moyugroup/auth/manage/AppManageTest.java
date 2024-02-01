package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.orm.model.App;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.util.KeyGeneratorUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

/**
 * AppManager 单测
 * <p>
 * Created by fanfan on 2024/02/01.
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@Import(AppManage.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AppManageTest {

    private final String testAppId = "app-test";

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    AppManage appManager;

    @Test
    public void getAppByIdTest() {
        String appSecret = KeyGeneratorUtil.generatorAppSecret();
        App app = new App();
        app.setAppId(testAppId);
        app.setAppName(testAppId);
        app.setAppSecret(appSecret);
        app.setAppAesKey("123");
        app.setIsActive(true);

        entityManager.persist(app);
        entityManager.flush();

        AppVO appById = appManager.getAppById(testAppId);
        Assert.notNull(appById, "未查询到 app");
    }

    @Test
    public void getAppByAppIdAndAppSecretTest() {
        String appSecret = KeyGeneratorUtil.generatorAppSecret();
        App app = new App();
        app.setAppId(testAppId);
        app.setAppName(testAppId);
        app.setAppSecret(appSecret);
        app.setAppAesKey("123");
        app.setIsActive(true);

        entityManager.persist(app);
        entityManager.flush();

        AppVO appByIdAndAppSecret = appManager.getAppByIdAndAppSecret(testAppId, appSecret);
        Assert.notNull(appByIdAndAppSecret, "未查询到 app");
    }
}
