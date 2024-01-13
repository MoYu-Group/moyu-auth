package io.github.moyugroup.auth.orm.model;

import io.github.moyugroup.spring.data.jpa.model.DeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * 用户表
 * <p>
 * Created by fanfan on 2024/01/13.
 */
@Setter
@Getter
@Entity
@Table(name = "`user`")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends DeletableEntity {

    /**
     * 用户ID，唯一
     */
    @Column(unique = true, length = 32, nullable = false)
    String userId;
    /**
     * 用户名，唯一
     */
    @Column(unique = true, length = 32, nullable = false)
    String username;
    /**
     * 密码
     */
    @Column(nullable = false)
    String password;
    /**
     * 手机号，唯一
     */
    @Column(unique = true, length = 32)
    String mobile;
    /**
     * 邮箱，唯一
     */
    @Column(unique = true, length = 48)
    String email;
    /**
     * 用户全名
     */
    @Column(length = 64)
    String fullName;
    /**
     * 用户状态
     */
    String userStatus;
    /**
     * 最后登录时间
     */
    LocalDateTime lastLoginTime;
}
