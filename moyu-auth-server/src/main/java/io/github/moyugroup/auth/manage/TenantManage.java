package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.orm.repository.TenantRepository;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 租户表 操作封装
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantManage {

    @Resource
    TenantRepository tenantRepository;

    /**
     * 根据租户ID获取租户名称
     *
     * @param tenantId 租户ID
     * @return 租户名称
     */
    public String getTenantNameByTenantId(String tenantId) {
        if (StringUtils.isBlank(tenantId)) {
            return null;
        }
        return tenantRepository.findTenantNameByTenantId(tenantId);
    }

}
