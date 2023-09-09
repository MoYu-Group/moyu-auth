package io.github.moyugroup.auth.demo.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.moyugroup.auth.demo.config.MoYuOAuthConstant;
import io.github.moyugroup.auth.demo.service.LoginCacheService;
import io.github.moyugroup.base.model.pojo.Result;
import io.github.moyugroup.enums.ErrorCodeEnum;
import io.github.moyugroup.util.AssertUtil;
import io.github.moyugroup.web.util.TraceIdMdcUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * MoYu-Auth 客户端注销成功处理器
 * <p>
 * Created by fanfan on 2023/09/09.
 */
@Slf4j
public class MoYuClientLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final LoginCacheService loginCacheService;

    private final SessionRegistry sessionRegistry;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 构造，用于注入 LoginCacheService
     *
     * @param loginCacheService
     */
    public MoYuClientLogoutSuccessHandler(LoginCacheService loginCacheService, SessionRegistry sessionRegistry) {
        this.loginCacheService = loginCacheService;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String ssoToken = getSsoToken(request);
        if (StringUtils.isBlank(ssoToken)) {
            super.onLogoutSuccess(request, response, authentication);
            return;
        }
        try {
            // TODO 进行调用方安全校验

            // 获取 ssoToken 登录的用户 sessionId
            String sessionIdBySsoToken = loginCacheService.getSessionIdBySsoToken(ssoToken);
            AssertUtil.hasText(sessionIdBySsoToken, "sessionId 不存在");

            // 注销已登录的 sessionId
            SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionIdBySsoToken);
            if (Objects.nonNull(sessionInformation)) {
                sessionInformation.expireNow();
                log.info("onLogoutSuccess ssoToken:{} sessionId:{} 已经退出登录", ssoToken, sessionIdBySsoToken);
            }
            buildJsonResponse(request, response, Result.success());
        } catch (Exception ex) {
            Result<String> fail = Result.fail(ErrorCodeEnum.USER_REQUEST_PARAMETER_ERROR.getCode(), ex.getMessage(), null);
            buildJsonResponse(request, response, fail);
        }
    }

    /**
     * 生成 json 返回值
     */
    private void buildJsonResponse(HttpServletRequest request, HttpServletResponse response, Result<?> result) {
        fillTraceId(result, request);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println(objectMapper.writeValueAsString(result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 填充返回值中的 TraceId
     *
     * @param result
     * @param request
     */
    private void fillTraceId(Result<?> result, HttpServletRequest request) {
        Object traceId = request.getAttribute(TraceIdMdcUtil.TRACE_ID);
        if (Objects.nonNull(traceId)) {
            result.setTraceId((String) traceId);
        }
    }

    /**
     * 获取 ssoToken
     *
     * @param request
     * @return
     */
    private String getSsoToken(HttpServletRequest request) {
        String ssoToken = request.getParameter(MoYuOAuthConstant.SSO_TOKEN_PARAM);
        if (StringUtils.isBlank(ssoToken)) {
            JSONObject requestJsonParam = getRequestJsonParam(request);
            ssoToken = requestJsonParam.getStr(MoYuOAuthConstant.SSO_TOKEN_PARAM);
        }
        return ssoToken;
    }

    /**
     * 解析请求体中的 JSON 参数
     *
     * @param request
     * @return
     */
    public JSONObject getRequestJsonParam(HttpServletRequest request) {
        JSONObject returnJson = new JSONObject();
        try {
            String contentType = request.getContentType();
            if (!StringUtils.equals("application/json", contentType)) {
                return returnJson;
            }
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder requestBody = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                requestBody.append(inputStr);
            }
            if (!JSONUtil.isTypeJSON(requestBody.toString())) {
                return returnJson;
            }
            return JSONUtil.parseObj(requestBody.toString());
        } catch (Exception ex) {
            log.error("onLogoutSuccess getRequestJson Error:{}", ex.getMessage());
            ex.printStackTrace();
            return returnJson;
        }
    }
}
