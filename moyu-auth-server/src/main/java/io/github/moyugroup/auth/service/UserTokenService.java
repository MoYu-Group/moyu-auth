package io.github.moyugroup.auth.service;

import io.github.moyugroup.auth.constant.enums.TokenTypeEnum;
import io.github.moyugroup.auth.manage.UserTokenManage;
import io.github.moyugroup.auth.orm.model.UserToken;
import io.github.moyugroup.auth.util.KeyGeneratorUtil;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户令牌服务
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTokenService {

    @Resource
    UserTokenManage userTokenManage;

    /**
     * 生成用户 accessToken，有效期5分钟
     *
     * @param userId
     * @return
     */
    public String generateSSOToken(String userId) {
        String accessToken = KeyGeneratorUtil.generateToken();
        UserToken userToken = new UserToken()
                .setUserId(userId)
                .setAccessToken(accessToken)
                .setTokenType(TokenTypeEnum.BEARER_TOKEN)
                .setExpireTime(LocalDateTime.now().plusMinutes(5));
        userTokenManage.userTokenSave(userToken);
        return accessToken;
    }

}
