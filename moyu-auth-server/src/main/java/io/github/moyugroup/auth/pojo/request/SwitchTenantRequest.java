package io.github.moyugroup.auth.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 切换租户参数
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SwitchTenantRequest {

    @NotBlank
    String tenantId;

    String backUrl;
}
