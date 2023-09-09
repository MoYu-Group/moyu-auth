package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.pojo.bo.UserLoginAppBO;
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
     * 保存用户登录过的App和发放的ssoToken信息
     * 用于退出登录时通知App用户退出登录
     *
     * @param userId
     * @param userLoginAppBO
     * @return
     */
    boolean saveUserLoginApp(Long userId, UserLoginAppBO userLoginAppBO);

    /**
     * 获取用户登录过的App列表
     *
     * @param userId
     * @return
     */
    List<UserLoginAppBO> getUserLoginAppList(Long userId);

}
