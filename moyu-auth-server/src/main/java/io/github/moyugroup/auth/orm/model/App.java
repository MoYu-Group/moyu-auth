package io.github.moyugroup.auth.orm.model;

import io.github.moyugroup.spring.data.jpa.model.DeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * 应用表实体
 * <p>
 * Created by fanfan on 2024/01/19.
 */
@Setter
@Getter
@Entity
@Table(name = "app", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"appId", "isDeleted"})
})
@SQLRestriction(value = "is_deleted = false")
@SQLDelete(sql = "UPDATE app SET is_deleted = true WHERE id = ?")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class App extends DeletableEntity {
    /**
     * 应用ID
     */
    @Column(length = 64, nullable = false)
    String appId;
    /**
     * 应用名称
     */
    @Column(length = 32, nullable = false)
    String appName;
    /**
     * 应用描述
     */
    String appDescription;
    /**
     * 应用密钥
     */
    @Column(nullable = false)
    String appSecret;
    /**
     * 应用 AES 加密 KEY
     */
    @Column(nullable = false)
    String appAesKey;
    /**
     * 重定向地址
     */
    String redirectUri;
    /**
     * 应用是否激活，默认激活
     */
    Boolean isActive = true;

}
