package io.github.moyugroup.auth.model.converter;

import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.model.vo.LoginUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户 转换器
 *
 * @author fanfan
 * @since 2025/03/12 00:05
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * 用户信息 转换为 登录用户信息
     *
     * @param user 用户信息
     * @return 登录用户信息
     */
    LoginUserVO toLoginUserVO(UserInfo user);
}
