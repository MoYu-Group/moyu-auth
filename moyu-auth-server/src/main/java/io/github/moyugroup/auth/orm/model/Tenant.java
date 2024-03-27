package io.github.moyugroup.auth.orm.model;

import io.github.moyugroup.spring.data.jpa.model.DeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * 租户表实体
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Setter
@Getter
@Entity
@Table(name = "`tenant`", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenantId", "isDeleted"}),
})
@Accessors(chain = true)
@SQLRestriction(value = "is_deleted = false")
@SQLDelete(sql = "UPDATE \"tenant\" SET is_deleted = true WHERE id = ?")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tenant extends DeletableEntity {
    /**
     * 租户ID
     */
    @Column(unique = true, length = 32, nullable = false)
    String tenantId;
    /**
     * 租户名称
     */
    @Column(length = 128, nullable = false)
    String tenantName;
    /**
     * 租户描述
     */
    String tenantDescription;
}
