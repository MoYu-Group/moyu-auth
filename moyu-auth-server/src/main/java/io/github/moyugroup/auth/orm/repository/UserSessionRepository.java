package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户登录会话 储存库接口
 * <p>
 * Created by fanfan on 2024/03/12.
 */
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

}
