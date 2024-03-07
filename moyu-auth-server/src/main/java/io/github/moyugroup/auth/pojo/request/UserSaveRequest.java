package io.github.moyugroup.auth.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户添加 入参对象
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Data
public class UserSaveRequest {

    /**
     * 用户名，唯一
     */
    @NotBlank
    String username;
    /**
     * 密码
     */
    @NotBlank
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
}
