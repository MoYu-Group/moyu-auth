package io.github.moyugroup.auth.common.context;

import io.github.moyugroup.auth.pojo.dto.UserInfo;

/**
 * 用户上下文
 * <p>
 * Created by fanfan on 2024/03/13.
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<>();

    public static void set(UserInfo user) {
        userThreadLocal.set(user);
    }

    public static UserInfo get() {
        return userThreadLocal.get();
    }

    public static void remove() {
        userThreadLocal.remove();
    }

}
