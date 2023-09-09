package io.github.moyugroup.auth.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.pojo.bo.UserLoginAppBO;
import io.github.moyugroup.auth.pojo.vo.OAuth2UserVO;
import io.github.moyugroup.auth.service.LoginCacheService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * OAuth 登录缓存，本地缓存实现
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@Slf4j
@Service
@ConditionalOnProperty(name = MoYuOAuthConstant.MOYU_AUTH_CACHE_FIELD_NAME, havingValue = MoYuOAuthConstant.MOYU_AUTH_CACHE_LOCAL, matchIfMissing = true)
public class LocalLoginCacheServiceImpl implements LoginCacheService {

    /**
     * 设置多久清理一次过期缓存
     */
    private static final long clearTime = 1000;

    /**
     * 缓存有效期为5分钟
     */
    private static final long expireIn5Min = 1000 * 60 * 5;

    /**
     * 缓存有效期为30分钟
     */
    private static final long expireIn30Min = 1000 * 60 * 30;
    /**
     * 储存用户ID 登录的 App 列表
     */
    public static final TimedCache<Long, Map<String, UserLoginAppBO>> userLoginApp = CacheUtil.newTimedCache(expireIn30Min);
    /**
     * 储存 ssoToken 与登录用户缓存
     */
    private static final TimedCache<String, OAuth2UserVO> ssoTokenUserCache = CacheUtil.newTimedCache(expireIn5Min);

    @PostConstruct
    private void init() {
        log.info("MoYu-Auth OAuth2 服务端登录缓存，使用本地缓存实现 --> LocalLoginCacheServiceImpl");
        // 每秒清理一次过期缓存
        ssoTokenUserCache.schedulePrune(clearTime);
        userLoginApp.schedulePrune(clearTime);
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
        ssoTokenUserCache.put(ssoToken, oAuth2UserVO);
        log.info("saveUserLoginTokenCache OAuth2UserVO:{} ssoToken:{}", oAuth2UserVO.toString(), ssoToken);
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
        OAuth2UserVO oAuth2UserVO = ssoTokenUserCache.get(ssoToken, false);
        if (Objects.nonNull(oAuth2UserVO)) {
            // 从缓存中取出有效数据后，删除缓存数据，使每条缓存只能读取一次
            ssoTokenUserCache.remove(ssoToken);
        }
        return oAuth2UserVO;
    }

    /**
     * 保存用户登录过的App和发放的ssoToken信息
     * 用于退出登录时通知App用户退出登录
     * TODO 可能有并发问题
     *
     * @param userId
     * @param userLoginAppBO
     * @return
     */
    @Override
    public boolean saveUserLoginApp(Long userId, UserLoginAppBO userLoginAppBO) {
        // 获取用户登录的所有应用信息
        Map<String, UserLoginAppBO> loginAppBOMap = userLoginApp.get(userId);
        if (CollectionUtils.isEmpty(loginAppBOMap)) {
            loginAppBOMap = new HashMap<>();
        }
        // 添加用户登录的新应用信息
        loginAppBOMap.put(userLoginAppBO.getAppId(), userLoginAppBO);
        // 更新用户登录应用缓存
        userLoginApp.put(userId, loginAppBOMap);
        log.info("saveUserLoginAppCache userId:{} loginApp:{} ssoToken:{}", userId, userLoginAppBO.getAppId(), userLoginAppBO.getSsoToken());
        return Boolean.TRUE;
    }

    /**
     * 获取用户登录过的App列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserLoginAppBO> getUserLoginAppList(Long userId) {
        Map<String, UserLoginAppBO> loginAppBOMap = userLoginApp.get(userId);
        if (CollectionUtils.isEmpty(loginAppBOMap)) {
            return Collections.emptyList();
        }
        Collection<UserLoginAppBO> values = loginAppBOMap.values();
        return values.stream().toList();
    }

    /**
     * 删除用户登录过的 APP 列表
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeUserLoginApp(Long userId) {
        userLoginApp.remove(userId);
        log.info("removeUserLoginAppCache userId:{}", userId);
        return Boolean.TRUE;
    }
}
