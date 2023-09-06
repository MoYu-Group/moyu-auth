package io.github.moyugroup.auth.demo.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.moyugroup.auth.demo.config.MoYuAuthConstant;
import io.github.moyugroup.auth.demo.pojo.vo.OAuth2UserVO;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import io.github.moyugroup.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * MoYu-Auth Http 请求封装
 * <p>
 * Created by fanfan on 2023/09/06.
 */
@Slf4j
public class OAuth2HttpUtil {

    /**
     * restTemplate 客户端
     */
    private static final RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

    /**
     * 使用OkHttpClient作为底层客户端
     *
     * @return
     */
    private static ClientHttpRequestFactory getClientHttpRequestFactory() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        return new OkHttp3ClientHttpRequestFactory(okHttpClient);
    }

    /**
     * 通过 ssoToken 请求 MoYu-Auth 获取登录用户信息
     *
     * @param appId
     * @param appSecret
     * @param grantType
     * @param ssoToken
     * @return
     */
    public static OAuth2UserVO getLoginUserByAccessToken(String url, String appId, String appSecret, String grantType, String ssoToken) {
        if (StringUtils.isAnyBlank(appId, appSecret, grantType, ssoToken)) {
            throw new BizException(ErrorCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject param = new JSONObject();
        param.set(MoYuAuthConstant.APP_ID_PARAM, appId);
        param.set(MoYuAuthConstant.APP_SECRET_PARAM, appSecret);
        param.set(MoYuAuthConstant.GRANT_TYPE_PARAM, grantType);
        param.set(MoYuAuthConstant.SSO_TOKEN_PARAM, ssoToken);
        HttpEntity requestEntity = new HttpEntity(param.toString(), headers);
        log.info("Request OAuth2 Server Url:{} Param:{}", url, param);
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, requestEntity, JSONObject.class);
        log.info("OAuth2 Server Response code:{} body:{}", response.getStatusCode(), response.getBody());
        if (HttpStatus.OK == response.getStatusCode()) {
            JSONObject body = response.getBody();
            AssertUtil.notNull(body, "OAuth2 Server Request Error");
            Boolean success = body.getBool("success");
            if (!success) {
                throw new BizException(ErrorCodeEnum.ERROR_CALLING_THIRD_PARTY_SERVICE.getCode(), body.getStr("message"));
            }
            JSONObject content = body.getJSONObject("content");
            return JSONUtil.toBean(content, OAuth2UserVO.class);
        }
        throw new BizException(ErrorCodeEnum.ERROR_CALLING_THIRD_PARTY_SERVICE);
    }
}
