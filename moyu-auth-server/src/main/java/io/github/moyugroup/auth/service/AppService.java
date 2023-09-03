package io.github.moyugroup.auth.service;

import cn.hutool.core.map.MapUtil;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * APP 服务
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
@Service
public class AppService {

    /**
     * 先在内存模拟数据
     */
    private Map<String, AppVO> map = MapUtil.of("demo-client", new AppVO().setAppId("demo-client"));

    /**
     * 根据 appId 查询
     *
     * @param appId
     * @return
     */
    public AppVO getAppById(String appId) {
        return map.get(appId);
    }
}
