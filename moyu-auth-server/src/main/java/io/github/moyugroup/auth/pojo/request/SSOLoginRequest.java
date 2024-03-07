package io.github.moyugroup.auth.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 校验 AccessToken 入参
 * <p>
 * Created by fanfan on 2023/09/05.
 */
@Data
public class SSOLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String loginType;

}
