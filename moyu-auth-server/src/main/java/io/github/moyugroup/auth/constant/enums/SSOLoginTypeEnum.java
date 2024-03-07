package io.github.moyugroup.auth.constant.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * SSO 登录类型
 * <p>
 * Created by fanfan on 2024/03/08.
 */
@Getter
public enum SSOLoginTypeEnum {

    ACCOUNT("ACCOUNT");

    private final String code;

    SSOLoginTypeEnum(String code) {
        this.code = code;
    }

    /**
     * 根据值查找
     *
     * @param value
     * @return
     */
    public static SSOLoginTypeEnum getByValue(String value) {
        return Arrays.stream(SSOLoginTypeEnum.values()).filter(x -> x.getCode().equals(value)).findFirst().orElse(null);
    }
}
