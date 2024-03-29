package io.github.moyugroup.auth.convert;

import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.orm.model.User;
import io.github.moyugroup.auth.pojo.request.UserSaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * User 转换器
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * UserSaveRequest 转为 User
     *
     * @param source
     * @return
     */
    User userSaveRequestToUser(UserSaveRequest source);

    /**
     * User 转为 UserInfo
     *
     * @param source
     * @return
     */
    UserInfo userToUserInfo(User source);
}
