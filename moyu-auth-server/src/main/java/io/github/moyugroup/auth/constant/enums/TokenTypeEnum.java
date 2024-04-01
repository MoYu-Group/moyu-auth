package io.github.moyugroup.auth.constant.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.moyugroup.base.model.enums.BaseEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * 令牌类型枚举
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum TokenTypeEnum implements BaseEnum {

    BEARER_TOKEN("BearerToken", "授权令牌"),
    REFRESH_TOKEN("RefreshToken", "刷新令牌"),
    ID_TOKEN("IDToken", "携带用户信息的 JWT Token"),
    ;

    /**
     * 枚举Code
     */
    final String code;
    /**
     * 枚举说明
     */
    final String message;

    /**
     * 返回Code
     *
     * @return code
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 返回描述
     *
     * @return desc
     */
    @Override
    public String getDesc() {
        return message;
    }
}
