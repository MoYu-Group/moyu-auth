package io.github.moyugroup.auth.pojo.vo;

import lombok.Data;

/**
 * OAuth 用户对象
 * 用于在OAuth登录过程中传递用户信息
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@Data
public class OAuthUserVO {

    private Long userId;

    private String tenantId;

    private String username;

}
