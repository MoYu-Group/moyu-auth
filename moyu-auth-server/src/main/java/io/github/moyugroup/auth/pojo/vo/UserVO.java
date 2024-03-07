package io.github.moyugroup.auth.pojo.vo;

import io.github.moyugroup.auth.constant.enums.UserStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户VO 对象
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Setter
@Getter
@Accessors(chain = true)
public class UserVO {
    /**
     * 用户ID，唯一
     */
    String userId;
    /**
     * 用户名，唯一
     */
    String username;
    /**
     * 密码
     */
    String password;
    /**
     * 手机号，唯一
     */
    String mobile;
    /**
     * 邮箱，唯一
     */
    String email;
    /**
     * 用户全名
     */
    String fullName;
    /**
     * 用户状态
     */
    UserStatusEnum userStatus;
    /**
     * 最后登录时间
     */
    LocalDateTime lastLoginTime;
}
