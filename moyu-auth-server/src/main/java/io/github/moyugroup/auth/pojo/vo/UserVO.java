package io.github.moyugroup.auth.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户对象
 * <p>
 * Created by fanfan on 2023/09/04.
 */
@Setter
@Getter
@Accessors(chain = true)
public class UserVO {

    private String username;
    private String password;
}
