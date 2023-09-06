package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.pojo.vo.OAuth2UserVO;

/**
 * OAuth 缓存服务
 * <p>
 * Created by fanfan on 2023/09/06.
 */
public interface OAuthCacheService {

    /**
     * 保存用户 ssoToken 登录信息
     * 有效期为5分钟
     *
     * @param ssoToken
     * @param oAuth2UserVO
     * @return
     */
    boolean setTokenWithUser(String ssoToken, OAuth2UserVO oAuth2UserVO);

    /**
     * 根据 ssoToken 获取登录用户信息
     *
     * @param ssoToken
     * @return
     */
    OAuth2UserVO getUserByToken(String ssoToken);

}
