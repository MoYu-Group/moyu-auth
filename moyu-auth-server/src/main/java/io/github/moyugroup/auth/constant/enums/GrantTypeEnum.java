package io.github.moyugroup.auth.constant.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * OAuth2 授权方式
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@Getter
public enum GrantTypeEnum {

    AUTHORIZATION_CODE("authorization_code");

    private final String code;

    GrantTypeEnum(String code) {
        this.code = code;
    }

    /**
     * 根据值查找
     *
     * @param value
     * @return
     */
    public static GrantTypeEnum getByValue(String value) {
        return Arrays.stream(GrantTypeEnum.values()).filter(x -> x.getCode().equals(value)).findFirst().orElse(null);
    }
}
