package io.github.moyugroup.auth.demo.pojo.vo;

import lombok.Data;

/**
 * OAuth2 用户对象
 * 用于在 OAuth2 登录过程中传递用户信息
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@Data
public class OAuth2UserVO {

    private Long userId;

    private String tenantId;

    private String username;

}
