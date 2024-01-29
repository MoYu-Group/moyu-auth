package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.pojo.vo.OAuth2UserVO;

import java.util.List;

/**
 * OAuth 缓存服务
 * <p>
 * Created by fanfan on 2023/09/06.
 */
public interface LoginCacheService {

    /**
     * 保存用户 ssoToken 登录信息
     * 有效期为5分钟
     *
     * @param ssoToken
     * @param oAuth2UserVO
     * @return
     */
    boolean saveUserLoginToken(String ssoToken, OAuth2UserVO oAuth2UserVO);

    /**
     * 根据 ssoToken 获取登录用户信息
     *
     * @param ssoToken
     * @return
     */
    OAuth2UserVO getLoginUserByToken(String ssoToken);

    /**
     * 保存用户登录过的 App 和发放的 ssoToken 信息
     * 用于退出登录时通知 App 用户退出登录
     *
     * @param userId
     * @param appId
     * @param ssoToken
     * @return
     */
    boolean saveUserLoginApp(String userId, String appId, String ssoToken);

    /**
     * 获取用户登录过的 App 列表
     *
     * @param userId
     * @return
     */
    List<String> getUserLoginAppList(String userId);

    /**
     * 删除用户登录过的 APP 列表
     *
     * @param userId
     * @return
     */
    boolean removeUserLoginApps(String userId);

}
