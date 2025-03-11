package io.github.moyugroup.auth.model.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 登录用户 VO
 *
 * @author fanfan
 * @since 2025/03/11 23:58
 */
@Setter
@Getter
@Builder
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginUserVO {
    /**
     * 租户ID
     */
    String tenantId;
    /**
     * 租户名称
     */
    String tenantName;
    /**
     * 用户ID
     */
    String userId;
    /**
     * 用户名
     */
    String username;
    /**
     * 昵称
     */
    String nickname;
}
