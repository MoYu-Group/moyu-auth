package io.github.moyugroup.auth.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.model.vo.OAuth2UserVO;
import io.github.moyugroup.auth.service.LoginCacheService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * OAuth 登录缓存，内存缓存实现
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@Slf4j
@Service
@ConditionalOnProperty(name = MoYuOAuthConstant.MOYU_AUTH_CACHE_FIELD_NAME, havingValue = MoYuOAuthConstant.MOYU_AUTH_CACHE_MEM, matchIfMissing = true)
public class MemLoginCacheServiceImpl implements LoginCacheService {

    /**
     * 储存用户登录的 App 列表
     * Key：用户ID
     * Value：登录的 AppId 与 ssoToken 列表
     */
    public static Cache<String, Map<String, String>> userLoginApps;

    /**
     * 储存 ssoToken 与登录用户缓存
     * Key：用户登录时下发的 ssoToken
     * Value：登录的用户对象
     */
    private static Cache<String, OAuth2UserVO> userLoginSSOTokenCache;

    @PostConstruct
    private void init() {
        log.info("MoYu-Auth OAuth2 服务端登录缓存，使用内存缓存实现 --> MemLoginCacheServiceImpl");

        // 初始化用户登录应用列表缓存对象，设置为30分钟后过期，以最后一次更新时间开始计算超时时间
        userLoginApps = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();

        // 初始化用户登录 ssoToken 缓存对象，设置为5分钟后过期，每次写入时重置过期时间
        userLoginSSOTokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    /**
     * 保存用户 ssoToken 登录信息
     * 有效期为5分钟
     *
     * @param ssoToken
     * @param oAuth2UserVO
     * @return
     */
    @Override
    public boolean saveUserLoginToken(String ssoToken, OAuth2UserVO oAuth2UserVO) {
        userLoginSSOTokenCache.put(ssoToken, oAuth2UserVO);
        log.info("saveUserLoginTokenCache OAuth2UserVO:{} ssoToken:{}", oAuth2UserVO, ssoToken);
        return Boolean.TRUE;
    }

    /**
     * 根据 ssoToken 获取登录信息
     *
     * @param ssoToken
     * @return
     */
    @Override
    public OAuth2UserVO getLoginUserByToken(String ssoToken) {
        OAuth2UserVO oAuth2UserVO = userLoginSSOTokenCache.getIfPresent(ssoToken);
        if (Objects.nonNull(oAuth2UserVO)) {
            // 从缓存中取出有效数据后，删除缓存数据，每条缓存只能读取一次
            userLoginSSOTokenCache.invalidate(ssoToken);
        }
        return oAuth2UserVO;
    }

    /**
     * 保存用户登录过的 App 和发放的 ssoToken 信息
     * 用于退出登录时通知 App 用户退出登录
     *
     * @param userId
     * @param appId
     * @param ssoToken
     * @return
     */
    @Override
    public boolean saveUserLoginApp(String userId, String appId, String ssoToken) {
        // 获取用户登录的所有应用信息
        Map<String, String> loginAppAndSSOTokenMap = userLoginApps.getIfPresent(userId);
        if (CollectionUtils.isEmpty(loginAppAndSSOTokenMap)) {
            loginAppAndSSOTokenMap = new HashMap<>();
        }
        // 添加用户登录的新应用信息
        loginAppAndSSOTokenMap.put(appId, ssoToken);
        // 更新用户登录应用缓存
        userLoginApps.put(userId, loginAppAndSSOTokenMap);
        log.info("saveUserLoginApp userId:{} loginApp:{} ssoToken:{}", userId, appId, ssoToken);
        return Boolean.TRUE;
    }

    /**
     * 获取用户登录过的AppId列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserLoginAppList(String userId) {
        Map<String, String> loginAppAndSSOTokenMap = userLoginApps.getIfPresent(userId);
        if (CollectionUtils.isEmpty(loginAppAndSSOTokenMap)) {
            return Collections.emptyList();
        }
        Collection<String> values = loginAppAndSSOTokenMap.values();
        return values.stream().toList();
    }

    /**
     * 删除用户登录过的 APP 列表
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeUserLoginApps(String userId) {
        userLoginApps.invalidate(userId);
        log.info("removeUserLoginAppCache userId:{}", userId);
        return Boolean.TRUE;
    }
}
