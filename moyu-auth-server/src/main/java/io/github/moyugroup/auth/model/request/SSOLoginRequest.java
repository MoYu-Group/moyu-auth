package io.github.moyugroup.auth.model.request;

import io.github.moyugroup.auth.constant.enums.SSOLoginTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private SSOLoginTypeEnum loginType;

}
