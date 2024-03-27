package io.github.moyugroup.auth.orm.repository;

import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.orm.model.TenantUser;
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
 * TenantUserRepository 单测
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
class TenantUserRepositoryTest {

    @Autowired
    TenantUserRepository tenantUserRepository;

    List<TenantUser> buildTestTenantUserList() {
        List<TenantUser> list = new ArrayList<>();
        list.add(new TenantUser().setTenantId("10001").setUserId("10000"));
        list.add(new TenantUser().setTenantId("10002").setUserId("10000"));
        list.add(new TenantUser().setTenantId("10003").setUserId("10000"));
        return list;
    }

    @Test
    void findByUserId() {
        List<TenantUser> tenantUsers = buildTestTenantUserList();
        tenantUserRepository.saveAll(tenantUsers);

        List<TenantUser> byUserId = tenantUserRepository.findByUserId("10000");
        log.debug("findByUserId:{}", JSONUtil.toJsonStr(byUserId));
        Assert.notEmpty(byUserId, "未查询到用户关联租户");
    }
}