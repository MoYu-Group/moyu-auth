package io.github.moyugroup.auth.orm.repository;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.orm.model.AppTenant;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * AppTenantRepository 单测
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
class AppTenantRepositoryTest {

    @Autowired
    AppTenantRepository appTenantRepository;

    @Test
    void findByAppIdTest() {
        List<AppTenant> appTenantList = new ArrayList<>();
        appTenantList.add(new AppTenant().setAppId("110001").setTenantId("10001"));
        appTenantList.add(new AppTenant().setAppId("110001").setTenantId("10002"));

        appTenantRepository.saveAll(appTenantList);

        List<AppTenant> appTenants = appTenantRepository.findByAppId("110001");
        log.debug("appTenants:{}", JSONUtil.toJsonStr(appTenants));
        Assert.notEmpty(appTenantList, "appTenants can not be empty!");
    }
}