package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.App;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 应用储存库接口
 * <p>
 * Created by fanfan on 2024/01/19.
 */
public interface AppRepository extends JpaRepository<App, Long> {

}
