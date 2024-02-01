package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.convert.AppConvert;
import io.github.moyugroup.auth.orm.model.App;
import io.github.moyugroup.auth.orm.repository.AppRepository;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * App 操作封装
 * Created by fanfan on 2024/02/01.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppManage {

    @Resource
    AppRepository appRepository;

    /**
     * 根据 appId 查询
     *
     * @param appId
     * @return
     */
    public AppVO getAppById(String appId) {
        App query = new App();
        query.setAppId(appId);
        App app = appRepository.getByApp(query);
        return AppConvert.INSTANCE.appToAppVO(app);
    }

    /**
     * 根据 appId 和 appSecret 查询
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public AppVO getAppByIdAndAppSecret(String appId, String appSecret) {
        App query = new App();
        query.setAppId(appId);
        query.setAppSecret(appSecret);
        App app = appRepository.getByApp(query);
        return AppConvert.INSTANCE.appToAppVO(app);
    }
}
