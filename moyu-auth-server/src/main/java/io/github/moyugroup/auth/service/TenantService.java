package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.manage.TenantManage;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 租户服务
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantService {

    @Resource
    TenantManage tenantManage;

    /**
     * 根据租户ID查询租户名称
     *
     * @param tenantId
     * @return
     */
    public String getTenantNameByTenantId(String tenantId) {
        return tenantManage.getTenantNameByTenantId(tenantId);
    }

}
