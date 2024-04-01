package io.github.moyugroup.auth.orm.model;

import io.github.moyugroup.auth.constant.enums.TokenTypeEnum;
import io.github.moyugroup.spring.data.jpa.model.BaseIdEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * 用户令牌表实体
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Setter
@Getter
@Entity
@ToString
@Accessors(chain = true)
@Table(name = "user_token")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserToken extends BaseIdEntity {
    /**
     * 用户ID
     */
    @Column(length = 32, nullable = false)
    String userId;
    /**
     * 访问令牌
     */
    @Column(nullable = false)
    String accessToken;
    /**
     * 刷新令牌
     */
    String refreshToken;
    /**
     * 令牌类型
     */
    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    TokenTypeEnum tokenType;
    /**
     * 过期时间
     */
    @Column(nullable = false)
    LocalDateTime expireTime;
}
