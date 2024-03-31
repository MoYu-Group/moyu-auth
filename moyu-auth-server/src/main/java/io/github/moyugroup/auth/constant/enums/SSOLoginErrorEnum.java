package io.github.moyugroup.auth.constant.enums;

import io.github.moyugroup.base.model.enums.ExceptionEnum;
import io.github.moyugroup.base.model.enums.ExceptionLevel;
import io.github.moyugroup.enums.ErrorCodeEnum;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

/**
 * OAuth 异常枚举
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum SSOLoginErrorEnum implements ExceptionEnum {
    APP_NOT_FOUND("A1100", "应用未在统一登录中心注册", ExceptionLevel.INFO),
    GRANT_TYPE_NOT_SUPPORT("A1101", "不支持的授权类型", ExceptionLevel.INFO),
    SSO_TOKEN_INVALID("A1102", "ssoToken 无效", ExceptionLevel.INFO),
    SSO_LOGIN_TYPE_INVALID("A1103", "loginType 无效", ExceptionLevel.INFO),
    SWITCH_TENANT_INVALID("A1104", "切换租户无效", ExceptionLevel.INFO),
    APP_CONFIG_ERROR("A1105", "应用配置错误，请检查应用配置", ExceptionLevel.INFO),
    APP_STATE_ERROR("A1106", "应用状态异常，请联系管理员", ExceptionLevel.INFO),

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
     * 异常消息
     */
    private final ExceptionLevel level;

    SSOLoginErrorEnum(String code, String message, ExceptionLevel level) {
        this.code = code;
        this.message = message;
        // 未指定的异常等级默认为 WRAN 级别
        this.level = Objects.isNull(level) ? ExceptionLevel.WARN : level;
    }

    /**
     * 根据 code 获取 message
     *
     * @param code ErrorCodeEnum.code
     * @return message
     */
    public static String getMessageByCode(String code) {
        ErrorCodeEnum[] values = ErrorCodeEnum.values();
        for (ErrorCodeEnum value : values) {
            if (Objects.equals(value.getCode(), code)) {
                return value.getMessage();
            }
        }
        return null;
    }

    /**
     * 返回异常编码
     *
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 返回异常消息
     *
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 返回异常等级
     *
     * @return level
     */
    @Override
    public ExceptionLevel getLevel() {
        return level;
    }
}
