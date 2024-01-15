package io.github.moyugroup.auth.constant.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.moyugroup.base.model.enums.BaseEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * 用户状态枚举
 * <p>
 * Created by fanfan on 2024/01/16.
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum UserStatusEnum implements BaseEnum {

    INACTIVE("Inactive", "未激活"),
    ACTIVE("Active", "已激活"),
    LOCKED("Locked", "已锁定"),
    RESTRICTED("Restricted", "账户受限"),
    FROZEN("Frozen", "冻结"),
    ;

    /**
     * 异常编码
     */
    final String code;
    /**
     * 异常消息
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
