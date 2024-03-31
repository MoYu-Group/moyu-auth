package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.orm.model.AppTenant;
import io.github.moyugroup.auth.orm.repository.AppTenantRepository;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用开通租户表数据操作封装
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppTenantManage {

    @Resource
    AppTenantRepository appTenantRepository;

    /**
     * 查询App关联租户列表
     *
     * @param appId
     * @return
     */
    public List<AppTenant> getByAppId(String appId) {
        return appTenantRepository.findByAppId(appId);
    }

    /**
     * 查询应用和租户关联信息
     *
     * @param appId
     * @param tenantId
     * @return
     */
    public AppTenant getByAppIdAndTenantId(String appId, String tenantId) {
        return appTenantRepository.findByAppIdAndTenantId(appId, tenantId);
    }

}
