package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.model.vo.SwitchTenantVO;
import io.github.moyugroup.auth.orm.model.TenantUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 租户用户关联 储存库接口
 * <p>
 * Created by fanfan on 2024/03/27.
 */
public interface TenantUserRepository extends JpaRepository<TenantUser, Long> {

    /**
     * 根据 UserId 查询列表
     *
     * @param userId
     * @return
     */
    List<TenantUser> findByUserId(String userId);

    /**
     * 查询用户和租户关联信息
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @return 关联信息
     */
    TenantUser findTenantUserByUserIdAndTenantId(String userId, String tenantId);

    /**
     * 查询用户关联租户列表，指定租户查询范围
     * todo 是否需要租户状态过滤
     *
     * @param userId
     * @return
     */
    @Query("SELECT new io.github.moyugroup.auth.model.vo.SwitchTenantVO(t.tenantId, t.tenantName) FROM TenantUser tu " +
            "INNER JOIN Tenant t ON tu.tenantId = t.tenantId and t.isDeleted = false " +
            "WHERE tu.userId = :userId and tu.tenantId in :tenantIds")
    List<SwitchTenantVO> getSwitchTenantVOsByUserIdAndTenantIdIn(@Param("userId") String userId, @Param("tenantIds") List<String> tenantIds);
}
