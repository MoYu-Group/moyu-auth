package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.manage.AppManage;
import io.github.moyugroup.auth.manage.AppTenantManage;
import io.github.moyugroup.auth.manage.TenantUserManage;
import io.github.moyugroup.auth.orm.model.AppTenant;
import io.github.moyugroup.auth.pojo.vo.AppVO;
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

    @Resource
    AppManage appManage;

    @Resource
    AppTenantManage appTenantManage;

    /**
     * 查询用户关联的可切换租户列表
     *
     * @param userId
     * @param appId
     * @return
     */
    public List<SwitchTenantVO> getSwitchTenantVOsByUserId(String userId, String appId) {
        // 获取请求的 App 信息，如果 App 不存在，则获取默认 App
        AppVO requestAppVO = appManage.getRequestAppVO(appId);
        // 获取App开通租户ID列表
        List<AppTenant> appTenantList = appTenantManage.getByAppId(requestAppVO.getAppId());
        List<String> tenantIds = appTenantList.stream().map(AppTenant::getTenantId).toList();
        // 查询用户关联租户列表，并指定查询租户范围
        return tenantUserManage.getSwitchTenantVOsByUserIdAndTenantIdIn(userId, tenantIds);
    }
}
