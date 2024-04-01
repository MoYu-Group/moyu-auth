package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * 用户登录会话 储存库接口
 * <p>
 * Created by fanfan on 2024/03/12.
 */
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    /**
     * 根据 SessionId 查询用户有效登录态
     *
     * @param sessionId 登录 sessionId
     * @param now       当前时间
     * @return 用户登录 UserSession
     */
    @Query("SELECT s FROM UserSession s WHERE s.sessionId = :sessionId AND s.expireTime > :now")
    UserSession findValidSessionBySessionId(@Param("sessionId") String sessionId, @Param("now") LocalDateTime now);

}
