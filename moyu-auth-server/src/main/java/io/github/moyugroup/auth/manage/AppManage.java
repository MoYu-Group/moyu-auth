package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.convert.AppConvert;
import io.github.moyugroup.auth.orm.model.App;
import io.github.moyugroup.auth.orm.repository.AppRepository;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * App 表数据操作封装
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
        App app = appRepository.findByApp(query);
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
        App app = appRepository.findByApp(query);
        return AppConvert.INSTANCE.appToAppVO(app);
    }

    /**
     * 获取登录请求的 App
     * 如果请求的 App 不存在，则获取默认 App
     *
     * @param appId
     * @return
     */
    public AppVO getRequestAppVO(String appId) {
        AppVO appById = getAppById(appId);
        if (Objects.isNull(appById)) {
            appById = getAppById(MoYuOAuthConstant.MOYU_AUTH);
        }
        return appById;
    }
}
