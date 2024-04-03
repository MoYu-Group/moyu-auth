package io.github.moyugroup.auth.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 校验ssoToken，并获取登录的用户信息
 * <p>
 * Created by fanfan on 2024/04/04.
 */
@Data
public class SSOGetUserRequest {

    @NotBlank
    private String appId;

    @NotBlank
    private String appSecret;

    @NotBlank
    private String ssoToken;

}
