package io.github.moyugroup.auth.facade;

import io.github.moyugroup.auth.pojo.vo.LoginUserVO;
import io.github.moyugroup.enums.ErrorCodeEnum;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户登录服务封装
 * <p>
 * Created by fanfan on 2023/08/04.
 */
@Slf4j
@Service
public class UserLoginFacade implements UserDetailsService {

    /**
     * 先在内存模拟数据
     * 用户:admin
     * 密码:123123
     */
    private final Map<String, LoginUserVO> userMap = new HashMap<>();

    @PostConstruct
    private void initUserMap() {
        userMap.put("admin", new LoginUserVO("admin", "$2a$10$l/VnIiaZyBiQj5Tt3SrHw..L5EJEthV6RmCzAfL87rVcqz6ExOtVC").setUserId(10001L).setTenantId("1000001"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserServiceImpl.loadUserByUsername with username:{}", username);
        LoginUserVO loginUserVO = userMap.get(username);
        if (Objects.isNull(loginUserVO)) {
            throw new UsernameNotFoundException(ErrorCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage());
        }
        return new LoginUserVO(loginUserVO.getUsername(), loginUserVO.getPassword()).setTenantId(loginUserVO.getTenantId()).setUserId(loginUserVO.getUserId());
    }
}
