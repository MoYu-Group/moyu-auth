package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户储存库接口
 * <p>
 * Created by fanfan on 2024/01/13.
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
