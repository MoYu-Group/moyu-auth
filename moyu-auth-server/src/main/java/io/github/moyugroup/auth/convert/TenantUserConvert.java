package io.github.moyugroup.auth.convert;

import io.github.moyugroup.auth.orm.model.TenantUser;
import io.github.moyugroup.auth.pojo.vo.SwitchTenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * TenantUser 转换器
 * <p>
 * Created by fanfan on 2024/03/27
 */
@Mapper
public interface TenantUserConvert {

    TenantUserConvert INSTANCE = Mappers.getMapper(TenantUserConvert.class);

    /**
     * TenantUser 转为 SwitchTenantVO
     *
     * @param source
     * @return
     */
    SwitchTenantVO tenantUserToSwitchTenantVO(TenantUser source);

    /**
     * TenantUsers 转为 SwitchTenantVOs
     *
     * @param source
     * @return
     */
    List<SwitchTenantVO> tenantUsersToSwitchTenantVOs(List<TenantUser> source);

}
