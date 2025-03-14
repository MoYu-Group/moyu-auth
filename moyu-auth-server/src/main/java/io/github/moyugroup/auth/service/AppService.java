package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.manage.AppManage;
import io.github.moyugroup.auth.model.vo.AppVO;
import io.github.moyugroup.auth.util.MoYuLoginUtil;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * APP 服务
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppService {

    @Resource
    AppManage appManager;

    /**
     * 根据 appId 查询
     *
     * @param appId
     * @return
     */
    public AppVO getAppById(String appId) {
        return appManager.getAppById(appId);
    }

    /**
     * 根据 AppId 和 AppSecret 查询
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public AppVO getByAppIdAndAppSecret(String appId, String appSecret) {
        return appManager.getAppByIdAndAppSecret(appId, appSecret);
    }

    /**
     * 查询应用是否存在，并校验应用是否可登录
     *
     * @param appId
     * @param appSecret
     */
    public void queryAndCheckApp(String appId, String appSecret) {
        AppVO appVO = getByAppIdAndAppSecret(appId, appSecret);
        MoYuLoginUtil.checkAppIsOk(appVO);
    }
}
