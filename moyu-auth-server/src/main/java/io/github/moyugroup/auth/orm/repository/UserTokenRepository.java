package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * 用户令牌 储存库接口
 * <p>
 * Created by fanfan on 2024/04/01.
 */
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    /**
     * 根据 ssoToken 查询有效 UserToken
     *
     * @param accessToken
     * @param now
     * @return
     */
    @Query("SELECT s FROM UserToken s WHERE s.accessToken = :accessToken AND s.expireTime > :now")
    UserToken getValidUserTokenByAccessToken(@Param("accessToken") String accessToken, @Param("now") LocalDateTime now);
}
