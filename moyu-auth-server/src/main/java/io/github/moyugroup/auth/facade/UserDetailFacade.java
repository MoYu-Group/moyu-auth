package io.github.moyugroup.auth.facade;

import cn.hutool.core.map.MapUtil;
import io.github.moyugroup.auth.pojo.vo.UserVO;
import io.github.moyugroup.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 用户服务
 * <p>
 * Created by fanfan on 2023/08/04.
 */
@Slf4j
@Service
public class UserDetailFacade implements UserDetailsService {

    /**
     * 先在内存模拟数据
     * 用户:admin
     * 密码:123123
     */
    private Map<String, UserVO> map = MapUtil.of("admin", new UserVO().setUsername("admin").setPassword("$2a$10$l/VnIiaZyBiQj5Tt3SrHw..L5EJEthV6RmCzAfL87rVcqz6ExOtVC"));

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserServiceImpl.loadUserByUsername with username:{}", username);
        UserVO userVO = map.get(username);
        if (Objects.isNull(userVO)) {
            throw new UsernameNotFoundException(ErrorCodeEnum.USER_ACCOUNT_DOES_NOT_EXIST.getMessage());
        }
        return User.withUsername(userVO.getUsername())
                .password(userVO.getPassword())
                .authorities("user", "admin")
                .build();
    }
}
