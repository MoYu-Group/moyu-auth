package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 用户 储存库接口
 * <p>
 * Created by fanfan on 2024/01/13.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据 User 实体查询
     *
     * @param queryUser
     * @return
     */
    default User getByUser(User queryUser) {
        Example<User> example = Example.of(queryUser);
        Optional<User> user = this.findOne(example);
        return user.orElse(null);
    }
}
