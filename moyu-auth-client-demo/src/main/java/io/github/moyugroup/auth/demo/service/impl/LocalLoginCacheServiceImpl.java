package io.github.moyugroup.auth.demo.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import io.github.moyugroup.auth.demo.config.MoYuOAuthConstant;
import io.github.moyugroup.auth.demo.service.LoginCacheService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 登录缓存服务，本地实现
 * <p>
 * Created by fanfan on 2023/09/09.
 */
@Slf4j
@Service
@ConditionalOnProperty(name = MoYuOAuthConstant.MOYU_AUTH_CACHE, havingValue = MoYuOAuthConstant.MOYU_AUTH_CACHE_LOCAL, matchIfMissing = true)
public class LocalLoginCacheServiceImpl implements LoginCacheService {

    /**
     * 设置多久清理一次过期缓存
     */
    private static final long clearTime = 1000;
    /**
     * 缓存有效期为30分钟，与 Spring Session 默认有效时间保持一致
     */
    private static final long expireIn30Min = 1000 * 60 * 30;
    /**
     * 储存 ssoToken 与登录用户 sessionId 缓存
     */
    public static final TimedCache<String, String> ssoTokenAndUserSessionIdCache = CacheUtil.newTimedCache(expireIn30Min);

    @PostConstruct
    private void init() {
        log.info("MoYu-Auth OAuth2  客户端登录缓存，使用本地缓存实现 --> LocalLoginCacheServiceImpl");
        // 每秒清理一次过期缓存
        ssoTokenAndUserSessionIdCache.schedulePrune(clearTime);
    }

    /**
     * 保存 ssoToken 登录的用户 sessionId
     *
     * @param ssoToken
     * @param sessionId
     */
    @Override
    public void saveSsoTokenAndSessionId(String ssoToken, String sessionId) {
        log.info("saveSsoTokenAndSessionId ssoToken:{} sessionId:{}", ssoToken, sessionId);
        ssoTokenAndUserSessionIdCache.put(ssoToken, sessionId);
    }

    /**
     * 根据 ssoToken 获取登录的用户 sessionId
     *
     * @param ssoToken
     * @return
     */
    @Override
    public String getSessionIdBySsoToken(String ssoToken) {
        String sessionId = ssoTokenAndUserSessionIdCache.get(ssoToken);
        log.info("getSessionIdBySsoToken ssoToken:{} sessionId:{}", ssoToken, sessionId);
        if (StringUtils.isNotBlank(sessionId)) {
            // 从缓存中取出有效数据后，删除缓存数据，使每条缓存只能读取一次
            ssoTokenAndUserSessionIdCache.remove(ssoToken);
            log.info("removeSsoTokenAndSessionIdCache ssoToken:{} sessionId:{}", ssoToken, sessionId);
        }
        return sessionId;
    }
}
