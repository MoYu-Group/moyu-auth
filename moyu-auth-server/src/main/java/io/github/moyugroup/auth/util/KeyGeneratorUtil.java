package io.github.moyugroup.auth.util;

import cn.hutool.core.util.RandomUtil;

/**
 * key 生成器
 * <p>
 * Created by fanfan on 2024/02/01.
 */
public class KeyGeneratorUtil {

    /**
     * 用于随机选的字符和数字（包括大写和小写字母）
     */
    public static final String BASE_CHAR_NUMBER = RandomUtil.BASE_CHAR.toUpperCase() + RandomUtil.BASE_CHAR_NUMBER_LOWER + "_";

    /**
     * 生成 AppSecret
     *
     * @return AppSecret
     */
    public static String generatorAppSecret() {
        return RandomUtil.randomString(BASE_CHAR_NUMBER, 64);
    }

    public static void main(String[] args) {
        String appSecret = generatorAppSecret();
        System.out.println(appSecret);
    }
}
