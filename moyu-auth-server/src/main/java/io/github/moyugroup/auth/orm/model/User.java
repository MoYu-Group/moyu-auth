package io.github.moyugroup.auth.orm.model;

import io.github.moyugroup.auth.constant.enums.UserStatusEnum;
import io.github.moyugroup.spring.data.jpa.model.DeletableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * 用户表
 * <p>
 * Created by fanfan on 2024/01/13.
 */
@Setter
@Getter
@Entity
@Table(name = "`user`", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username", "isDeleted"}),
        @UniqueConstraint(columnNames = {"mobile", "isDeleted"}),
        @UniqueConstraint(columnNames = {"email", "isDeleted"}),
})
@SQLRestriction(value = "is_deleted = false")
@SQLDelete(sql = "UPDATE \"user\" SET is_deleted = true WHERE id = ?")
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
    @Column(length = 32, nullable = false)
    String username;
    /**
     * 密码
     */
    @Column(nullable = false)
    String password;
    /**
     * 手机号，唯一
     */
    @Column(length = 32)
    String mobile;
    /**
     * 邮箱，唯一
     */
    @Column(length = 48)
    String email;
    /**
     * 用户全名
     */
    @Column(length = 64)
    String fullName;
    /**
     * 用户状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 64)
    UserStatusEnum userStatus;
    /**
     * 最后登录时间
     */
    LocalDateTime lastLoginTime;
}
