package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.manage.TenantUserManage;
import io.github.moyugroup.auth.pojo.vo.SwitchTenantVO;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户用户关联 服务
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantUserService {

    @Resource
    TenantUserManage tenantUserManage;

    /**
     * 查询用户关联的可切换租户列表
     *
     * @param userId
     * @return
     */
    public List<SwitchTenantVO> getSwitchTenantVOsByUserId(String userId) {
        return tenantUserManage.getSwitchTenantVOsByUserId(userId);
    }
}
