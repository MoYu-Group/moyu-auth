package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.Tenant;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

/**
 * TenantRepository 单测
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
class TenantRepositoryTest {

    @Resource
    TenantRepository tenantRepository;

    Tenant buildTenant() {
        return new Tenant().setTenantId("10001").setTenantName("测试租户").setTenantDescription("desc");
    }

    @Test
    void findTenantNameByTenantIdTest() {
        Tenant tenant = buildTenant();

        tenantRepository.save(tenant);

        String tenantNameByTenantId = tenantRepository.findTenantNameByTenantId(tenant.getTenantId());
        log.debug("租户ID：{} 租户名称：{}", tenant.getTenantId(), tenantNameByTenantId);
        Assert.hasText(tenantNameByTenantId, "未查询到租户名称");

    }
}