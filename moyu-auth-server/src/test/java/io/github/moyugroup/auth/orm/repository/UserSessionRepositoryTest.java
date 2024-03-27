package io.github.moyugroup.auth.orm.repository;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.constant.MoYuLoginConstant;
import io.github.moyugroup.auth.orm.model.UserSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * UserSessionRepositoryTest 单测
 * <p>
 * Created by fanfan on 2024/03/27.
 */
@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserSessionRepositoryTest {

    @Autowired
    UserSessionRepository userSessionRepository;

    UserSession buildTestUserSession() {
        LocalDateTime now = LocalDateTime.now();
        return new UserSession()
                .setSessionId(IdUtil.fastSimpleUUID().toUpperCase())
                .setUserId("10000")
                .setCreatedTime(now)
                .setExpiresTime(now.plusSeconds(MoYuLoginConstant.LOGIN_EXPIRE_SECONDS))
                .setUserIp(null)
                .setUserAgent(null);
    }

    @Test
    void findValidSessionBySessionId() {
        UserSession userSession = buildTestUserSession();
        log.debug("insert UserSession:{}", JSONUtil.toJsonStr(userSession));
        userSessionRepository.save(userSession);

        UserSession validSessionBySessionId = userSessionRepository.findValidSessionBySessionId(userSession.getSessionId(), LocalDateTime.now());
        log.debug("validSessionBySessionId:{}", JSONUtil.toJsonStr(validSessionBySessionId));

        Assert.notNull(validSessionBySessionId, "validSessionBySessionId is null!");
    }
}