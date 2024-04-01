package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户令牌 储存库接口
 * <p>
 * Created by fanfan on 2024/04/01.
 */
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

}
