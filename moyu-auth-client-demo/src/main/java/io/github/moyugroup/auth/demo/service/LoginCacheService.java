package io.github.moyugroup.auth.demo.service;

/**
 * 登录缓存服务
 * <p>
 * Created by fanfan on 2023/09/09.
 */
public interface LoginCacheService {

    /**
     * 保存 ssoToken 登录的用户 sessionId
     *
     * @param ssoToken
     * @param sessionId
     */
    void saveSsoTokenAndSessionId(String ssoToken, String sessionId);

    /**
     * 根据 ssoToken 获取登录的用户 sessionId
     *
     * @param ssoToken
     * @return
     */
    String getSessionIdBySsoToken(String ssoToken);

}
