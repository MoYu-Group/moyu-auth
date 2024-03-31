package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.AppTenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 应用开通租户 储存库接口
 * <p>
 * Created by fanfan on 2024/04/01.
 */
public interface AppTenantRepository extends JpaRepository<AppTenant, Long> {

    /**
     * 根据 appId 查询个关联列表
     *
     * @param appId
     * @return
     */
    List<AppTenant> findByAppId(String appId);

    /**
     * 查询应用和租户关联信息
     *
     * @param appId
     * @param tenantId
     * @return
     */
    AppTenant findByAppIdAndTenantId(String appId, String tenantId);

}
