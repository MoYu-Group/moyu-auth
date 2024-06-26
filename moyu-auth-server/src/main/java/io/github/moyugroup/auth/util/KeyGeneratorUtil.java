package io.github.moyugroup.auth.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

import java.security.SecureRandom;

/**
 * key 生成器
 * 用于生成由随机字符和数字组成的 Key
 * <p>
 * Created by fanfan on 2024/02/01.
 */
public class KeyGeneratorUtil {

    private static final SecureRandom random = new SecureRandom();

    /**
     * 用于随机选的字符和数字（包括大写和小写字母）
     */
    public static final String BASE_CHAR_NUMBER = RandomUtil.BASE_CHAR.toUpperCase() + RandomUtil.BASE_CHAR_NUMBER_LOWER + "_";

    /**
     * 生成64位 AppSecret
     *
     * @return 64位 AppSecret
     */
    public static String generatorAppSecret() {
        return RandomUtil.randomString(BASE_CHAR_NUMBER, 64);
    }

    /**
     * Token使用UUID
     *
     * @return UUID
     */
    public static String generateToken() {
        return IdUtil.fastSimpleUUID();
    }

}
