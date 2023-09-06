package io.github.moyugroup.auth.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import io.github.moyugroup.auth.constant.MoYuAuthOAuthConstant;
import io.github.moyugroup.auth.pojo.vo.OAuthUserVO;
import io.github.moyugroup.auth.service.OAuthCacheService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * OAuth 登录缓存，本地缓存实现
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@Slf4j
@Service
@ConditionalOnProperty(name = MoYuAuthOAuthConstant.MOYU_AUTH_CACHE, havingValue = MoYuAuthOAuthConstant.MOYU_AUTH_CACHE_LOCAL, matchIfMissing = true)
public class OAuthLocalCacheServiceImpl implements OAuthCacheService {

    /**
     * 缓存有效期为5分钟
     */
    private static final long expireTime = 1000 * 60 * 5;

    /**
     * 储存 ssoToken 与登录用户缓存
     */
    private static final TimedCache<String, OAuthUserVO> ssoTokenUserCache = CacheUtil.newTimedCache(expireTime);

    @PostConstruct
    private void init() {
        log.info("OAuth2 登录缓存，使用本地缓存实现 --> OAuthLocalCacheServiceImpl");
    }

    /**
     * 保存用户 ssoToken 登录信息
     * 有效期为5分钟
     *
     * @param ssoToken
     * @param oAuthUserVO
     * @return
     */
    @Override
    public boolean setTokenWithUser(String ssoToken, OAuthUserVO oAuthUserVO) {
        ssoTokenUserCache.put(ssoToken, oAuthUserVO);
        return Boolean.TRUE;
    }

    /**
     * 根据 ssoToken 获取登录信息
     *
     * @param ssoToken
     * @return
     */
    @Override
    public OAuthUserVO getUserByToken(String ssoToken) {
        OAuthUserVO oAuthUserVO = ssoTokenUserCache.get(ssoToken, false);
        if (Objects.isNull(oAuthUserVO)) {
            return null;
        }
        // 从缓存中取出有效数据后，删除缓存数据，使每条缓存只能读取一次
        ssoTokenUserCache.remove(ssoToken);
        return oAuthUserVO;
    }

}
