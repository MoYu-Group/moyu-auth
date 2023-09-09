package io.github.moyugroup.auth.util;

import cn.hutool.json.JSONObject;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
     * RestTemplate 客户端
     */
    private static final RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

    /**
     * 使用 OkHttpClient 作为底层客户端
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
     * 通知应用用户注销
     *
     * @param url
     * @param ssoToken
     */
    public static void notifyAppUserLogoutByToken(String url, String ssoToken) {
        if (StringUtils.isAnyBlank(url, ssoToken)) {
            throw new BizException(ErrorCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY);
        }
        JSONObject param = new JSONObject();
        param.set(MoYuOAuthConstant.SSO_TOKEN_PARAM, ssoToken);
        sendPostHttpRequest(url, param);
    }

    /**
     * 发送 Http Post 请求
     * TODO 发送异步请求
     *
     * @param url
     * @param param
     * @return
     */
    private static ResponseEntity<JSONObject> sendPostHttpRequest(String url, JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(param.toString(), headers);
        log.info("OAuth2 Server Request Url:{} Param:{}", url, param);
        ResponseEntity<Object> response = restTemplate.postForEntity(url, requestEntity, Object.class);
//        log.info("OAuth2 Server Response code:{} body:{}", response.getStatusCode(), response.getBody());
//        return response;
        return null;
    }
}
