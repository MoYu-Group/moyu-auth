package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.pojo.vo.AppVO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * APP 服务
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
@Service
public class AppService {

    /**
     * 先在内存模拟 APP 数据
     */
    private final Map<String, AppVO> map = new HashMap<>();

    @PostConstruct
    private void initMap() {
        map.put("demo-client", new AppVO().setAppId("demo-client").setAppSecret("123123").setAppUrl("http://demo.ffis.me:8001"));
        map.put("moyu-auth", new AppVO().setAppId("moyu-auth").setAppSecret("123123"));
    }

    /**
     * 根据 appId 查询
     *
     * @param appId
     * @return
     */
    public AppVO getAppById(String appId) {
        return map.get(appId);
    }

    /**
     * 根据 AppId 和 AppSecret 查询
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public AppVO getByAppIdAndAppSecret(String appId, String appSecret) {
        AppVO appById = getAppById(appId);
        if (Objects.isNull(appById)) {
            return null;
        }
        if (appById.getAppSecret().equals(appSecret)) {
            return appById;
        }
        return null;
    }
}
