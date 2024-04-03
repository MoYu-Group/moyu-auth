package io.github.moyugroup.auth.manage;

import io.github.moyugroup.auth.orm.model.UserToken;
import io.github.moyugroup.auth.orm.repository.UserTokenRepository;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户令牌表数据操作封装
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTokenManage {

    @Resource
    UserTokenRepository userTokenRepository;

    /**
     * 用户 Token 新增/编辑
     *
     * @param userToken
     * @return
     */
    public UserToken userTokenSave(UserToken userToken) {
        return userTokenRepository.save(userToken);
    }

    /**
     * 根据 accessToken 查询有效 UserToken
     *
     * @param accessToken
     * @return
     */
    public UserToken getValidUserTokenByAccessToken(String accessToken) {
        return userTokenRepository.getValidUserTokenByAccessToken(accessToken, LocalDateTime.now());
    }

    /**
     * 删除 userToken
     *
     * @param id
     */
    public void removeUserTokenById(Long id) {
        userTokenRepository.deleteById(id);
    }

}
