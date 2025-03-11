package io.github.moyugroup.auth.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回需要重定向的地址
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@Data
@Accessors(chain = true)
public class RedirectUrlVO {

    /**
     * 重定向地址
     */
    private String redirectUrl;

}
