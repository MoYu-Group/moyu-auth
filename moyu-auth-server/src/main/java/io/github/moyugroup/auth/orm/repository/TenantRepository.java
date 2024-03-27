package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 租户 储存库接口
 * <p>
 * Created by fanfan on 2024/03/27.
 */
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    /**
     * 根据租户ID查询租户名称
     *
     * @param tenantId 租户ID
     * @return 租户名称
     */
    @Query("SELECT t.tenantName FROM Tenant t WHERE t.tenantId = :tenantId")
    String findTenantNameByTenantId(@Param("tenantId") String tenantId);

}
