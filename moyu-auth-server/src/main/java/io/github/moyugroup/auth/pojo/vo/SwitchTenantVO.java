package io.github.moyugroup.auth.pojo.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 切换租户列表
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SwitchTenantVO {

    String tenantId;

    String tenantName;
}
