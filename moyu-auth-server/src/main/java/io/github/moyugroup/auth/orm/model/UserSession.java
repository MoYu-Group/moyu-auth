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

import java.time.LocalDateTime;

/**
 * 用户登录会话表 实体
 * <p>
 * Created by fanfan on 2024/03/12.
 */
@Setter
@Getter
@Entity
@ToString
@Accessors(chain = true)
@Table(name = "user_session")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSession extends BaseIdEntity {
    /**
     * 会话ID, 唯一
     */
    @Column(nullable = false, unique = true)
    String sessionId;
    /**
     * 用户ID
     */
    @Column(nullable = false, length = 32)
    String userId;
    /**
     * 租户ID
     */
    @Column(length = 32)
    String tenantId;
    /**
     * 会话创建时间
     */
    @Column(nullable = false)
    LocalDateTime createdTime;
    /**
     * 会话过期时间
     */
    @Column(nullable = false)
    LocalDateTime expiresTime;
    /**
     * 用户IP地址
     */
    @Column(length = 32)
    String userIp;
    /**
     * 用户浏览器/客户端信息
     */
    @Column
    String userAgent;
}
