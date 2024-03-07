package io.github.moyugroup.auth.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户ID生成器
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Slf4j
public class UserIdGeneratorUtil {

    private static long currentUserId = 10000; // 初始值设为 10000

    public static synchronized String getNextUserId() {
        return String.valueOf(currentUserId++);
    }

}
