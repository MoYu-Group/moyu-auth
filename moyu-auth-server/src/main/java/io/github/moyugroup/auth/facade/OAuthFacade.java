package io.github.moyugroup.auth.facade;

import io.github.moyugroup.auth.constant.enums.GrantTypeEnum;
import io.github.moyugroup.auth.constant.enums.OAuth2ErrorEnum;
import io.github.moyugroup.auth.pojo.request.AccessTokenRequest;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.pojo.vo.OAuthUserVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.service.OAuthCacheService;
import io.github.moyugroup.util.AssertUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * OAuth
 * <p>
 * Created by fanfan on 2023/09/05.
 */
@Slf4j
@Service
public class OAuthFacade {

    @Resource
    private AppService appService;

    @Resource
    private OAuthCacheService oAuthCacheService;

    /**
     * accessToken 校验
     * 校验成功返回用户信息
     *
     * @param accessTokenRequest
     * @return
     */
    public OAuthUserVO accessToken(AccessTokenRequest accessTokenRequest) {
        // 应用有效性判断
        AppVO appVO = appService.getByAppIdAndAppSecret(accessTokenRequest.getAppId(), accessTokenRequest.getAppSecret());
        AssertUtil.notNull(appVO, OAuth2ErrorEnum.APP_NOT_FOUND);

        // 授权类型判断
        GrantTypeEnum grantTypeEnum = GrantTypeEnum.getByValue(accessTokenRequest.getGrantType());
        AssertUtil.notNull(grantTypeEnum, OAuth2ErrorEnum.GRANT_TYPE_NOT_SUPPORT);

        // 根据 ssoToken 查找登录用户 TODO 异常判断
        return oAuthCacheService.getUserByToken(accessTokenRequest.getSsoToken());
    }

}
