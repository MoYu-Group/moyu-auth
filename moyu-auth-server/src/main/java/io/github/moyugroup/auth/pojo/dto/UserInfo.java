package io.github.moyugroup.auth.pojo.dto;

import io.github.moyugroup.auth.constant.enums.UserStatusEnum;
import lombok.Data;

/**
 * 用户信息
 * <p>
 * Created by fanfan on 2024/03/13.
 */
@Data
public class UserInfo {
    /**
     * 用户ID
     */
    String userId;
    /**
     * 用户名
     */
    String username;
    /**
     * 手机号
     */
    String mobile;
    /**
     * 邮箱
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
}
