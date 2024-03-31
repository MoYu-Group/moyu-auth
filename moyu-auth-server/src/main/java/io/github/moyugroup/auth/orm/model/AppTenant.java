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
 * 应用开通租户表实体
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@Setter
@Getter
@Entity
@ToString
@Accessors(chain = true)
@Table(name = "app_tenant")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppTenant extends BaseIdEntity {
    /**
     * 租户ID
     */
    @Column(length = 32, nullable = false)
    String appId;
    /**
     * 用户ID
     */
    @Column(length = 32, nullable = false)
    String tenantId;
}
