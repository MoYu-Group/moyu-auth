package io.github.moyugroup.auth.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

/**
 * 用户对象
 * <p>
 * Created by fanfan on 2023/09/04.
 */
@Setter
@Getter
@Accessors(chain = true)
public class UserVO extends User {

    private Long userId;

    private String tenantId;


    public UserVO(String username, String password) {
        super(username, password, new ArrayList<>());
    }

}
