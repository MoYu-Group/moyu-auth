package io.github.moyugroup.auth.pojo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * APP 对象
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Setter
@Getter
@Accessors(chain = true)
public class AppVO {

    private String appId;

    private String appSecret;

    private String ssoCallBackUrl;
}
