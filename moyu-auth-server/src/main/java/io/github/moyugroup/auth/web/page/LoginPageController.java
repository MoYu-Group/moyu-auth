package io.github.moyugroup.auth.web.page;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.orm.model.UserSession;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.pojo.vo.SwitchTenantVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.auth.service.SSOLoginService;
import io.github.moyugroup.auth.service.TenantUserService;
import io.github.moyugroup.auth.service.UserTokenService;
import io.github.moyugroup.auth.util.MoYuLoginUtil;
import io.github.moyugroup.auth.util.UrlUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * 登录相关页面渲染
 * <p>
 * Created by fanfan on 2023/08/02.
 */
@Slf4j
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginPageController {

    @Resource
    AppService appService;

    @Resource
    TenantUserService tenantUserService;

    @Resource
    SSOLoginService ssoLoginService;

    @Resource
    UserTokenService userTokenService;

    /**
     * 登录页渲染
     * 如果已经登录，则重定向到应用或者
     *
     * @param model    页面模型
     * @param request  请求对象
     * @param response 响应对象
     * @return 页面模版名称
     */
    @RequestMapping(value = SSOLoginConstant.LOGIN_PAGE_PATH, method = {RequestMethod.GET, RequestMethod.POST})
    public String ssoLogin(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取登录应用
        String appId = MoYuLoginUtil.getRequestParamAppId(request);
        String backUrl = request.getParameter(SSOLoginConstant.BACK_URL);
        AppVO appVO = appService.getAppById(appId);

        UserSession userLoginSession = ssoLoginService.getUserLoginSession(request);
        if (Objects.isNull(userLoginSession)) {
            // 未登录，渲染登录页面
            MoYuLoginUtil.loadLoginPage(model, appVO);
            return "ssoLogin";
        }
        // 已登录，判断是否选择租户
        if (StringUtils.isBlank(userLoginSession.getTenantId())) {
            // 未选择租户，重定向到选择租户页面
            response.sendRedirect(SSOLoginConstant.SWITCH_TENANT_PATH + UrlUtil.getRedirectParam(request));
            return null;
        }
        // 已选择租户，判断登录应用类型
        if (MoYuLoginUtil.checkIsMoYuAuthApp(appId)) {
            // 一方应用，直接重定向回首页
            response.sendRedirect(SSOLoginConstant.INDEX_PAGE_PATH);
            return null;
        } else {
            // 二方应用，发放sso令牌，并携带sso参数重定向回应用，建立应用登录态
            String ssoToken = userTokenService.generateSSOToken(userLoginSession.getSessionId());
            StringBuilder url = new StringBuilder();
            url.append(UrlUtil.getOAuthSSOTokenHandlerUrl(appVO.getRedirectUri()))
                    .append("?").append(SSOLoginConstant.SSO_TOKEN).append("=").append(ssoToken);
            if (StringUtils.isNoneBlank(backUrl)) {
                url.append("&").append(SSOLoginConstant.BACK_URL).append("=").append(URLEncoder.encode(backUrl, StandardCharsets.UTF_8));
            }
            response.sendRedirect(url.toString());
            return null;
        }
    }

    /**
     * 选择租户页面渲染
     * todo 如果只有一个租户，则选择该租户，并302到登录页面
     *
     * @param model    页面模型
     * @param request  请求对象
     * @param response 响应对象
     * @return 页面模版名称
     */
    @RequestMapping(value = SSOLoginConstant.SWITCH_TENANT_PATH, method = {RequestMethod.GET, RequestMethod.POST})
    public String switchTenant(Model model, HttpServletRequest request, HttpServletResponse response) {
        List<SwitchTenantVO> switchTenantVOS = tenantUserService.getSwitchTenantVOsByUserId(UserContext.getCurUserId(),
                MoYuLoginUtil.getRequestParamAppId(request));
        model.addAttribute("tenantList", switchTenantVOS);
        return "switchTenant";
    }

}
