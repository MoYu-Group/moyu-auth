package io.github.moyugroup.auth.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 * <p>
 * Created by fanfan on 2023/08/04.
 */
@Slf4j
@Service
public class UserDetailFacade implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserServiceImpl.loadUserByUsername with username:{}", username);
        return User.withUsername("admin")
                // 123123
                .password("$2a$10$l/VnIiaZyBiQj5Tt3SrHw..L5EJEthV6RmCzAfL87rVcqz6ExOtVC")
                .authorities("user", "admin")
                .build();
    }
}
