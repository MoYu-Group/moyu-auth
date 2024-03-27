package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.orm.model.TenantUser;
import io.github.moyugroup.auth.orm.repository.TenantUserRepository;
import io.github.moyugroup.auth.pojo.vo.SwitchTenantVO;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户用户关联表 操作封装
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantUserManage {

    @Resource
    TenantUserRepository tenantUserRepository;

    /**
     * 根据 UserId 查询
     *
     * @param userId
     * @return
     */
    public List<SwitchTenantVO> getSwitchTenantVOsByUserId(String userId) {
        return tenantUserRepository.findSwitchTenantVOListByUserId(userId);
    }

    /**
     * 查询用户和租户关联信息
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @return 关联信息
     */
    public TenantUser findTenantUserByUserIdAndTenantId(String userId, String tenantId) {
        return tenantUserRepository.findTenantUserByUserIdAndTenantId(userId, tenantId);
    }
}
