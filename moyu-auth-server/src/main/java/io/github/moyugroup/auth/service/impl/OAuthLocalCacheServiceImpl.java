package io.github.moyugroup.auth.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import io.github.moyugroup.auth.constant.MoYuAuthOAuthConstant;
import io.github.moyugroup.auth.constant.enums.OAuth2ErrorEnum;
import io.github.moyugroup.auth.pojo.vo.OAuth2UserVO;
import io.github.moyugroup.auth.service.OAuthCacheService;
import io.github.moyugroup.util.AssertUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

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
    private static final TimedCache<String, OAuth2UserVO> ssoTokenUserCache = CacheUtil.newTimedCache(expireTime);

    @PostConstruct
    private void init() {
        log.info("OAuth2 登录缓存，使用本地缓存实现 --> OAuthLocalCacheServiceImpl");
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
    public boolean setTokenWithUser(String ssoToken, OAuth2UserVO oAuth2UserVO) {
        ssoTokenUserCache.put(ssoToken, oAuth2UserVO);
        return Boolean.TRUE;
    }

    /**
     * 根据 ssoToken 获取登录信息
     *
     * @param ssoToken
     * @return
     */
    @Override
    public OAuth2UserVO getUserByToken(String ssoToken) {
        OAuth2UserVO oAuth2UserVO = ssoTokenUserCache.get(ssoToken, false);
        AssertUtil.notNull(oAuth2UserVO, OAuth2ErrorEnum.SSO_TOKEN_INVALID);
        // 从缓存中取出有效数据后，删除缓存数据，使每条缓存只能读取一次
        ssoTokenUserCache.remove(ssoToken);
        return oAuth2UserVO;
    }

}
