package io.github.moyugroup.auth.orm.model;

import io.github.moyugroup.spring.data.jpa.model.BaseIdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * 租户用户关联表实体
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Setter
@Getter
@Entity
@ToString
@Accessors(chain = true)
@Table(name = "tenant_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantUser extends BaseIdEntity {
    /**
     * 租户ID
     */
    @Column(length = 32, nullable = false)
    String tenantId;
    /**
     * 用户ID
     */
    @Column(length = 32, nullable = false)
    String userId;
}
