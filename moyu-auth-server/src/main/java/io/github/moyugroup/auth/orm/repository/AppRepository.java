package io.github.moyugroup.auth.orm.repository;

import io.github.moyugroup.auth.orm.model.App;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 应用 储存库接口
 * <p>
 * Created by fanfan on 2024/01/19.
 */
public interface AppRepository extends JpaRepository<App, Long> {

    /**
     * 根据 APP 实体查询
     *
     * @param queryApp
     * @return
     */
    default App findByApp(App queryApp) {
        Example<App> example = Example.of(queryApp);
        Optional<App> app = this.findOne(example);
        return app.orElse(null);
    }
}
