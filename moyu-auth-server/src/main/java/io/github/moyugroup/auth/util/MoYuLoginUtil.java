package io.github.moyugroup.auth.util;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import io.github.moyugroup.auth.constant.enums.SSOLoginErrorEnum;
import io.github.moyugroup.auth.model.vo.AppVO;
import io.github.moyugroup.util.AssertUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

/**
 * 登录过程 工具类封装
 * <p>
 * Created by fanfan on 2023/09/03.
 */
@Slf4j
public class MoYuLoginUtil {

    /**
     * 获取请求的 AppId
     * 如未传 AppId，则视为一方应用登录，设置为系统默认的 AppId
     *
     * @param request
     * @return
     */
    public static String getRequestParamAppId(HttpServletRequest request) {
        String appId = request.getParameter(SSOLoginConstant.APP_ID);
        if (StringUtils.isBlank(appId)) {
            appId = MoYuOAuthConstant.MOYU_AUTH;
        }
        return appId;
    }

    /**
     * 检查登录 APP 信息，如果不符合登录要求则报错
     *
     * @param appVO
     */
    public static void checkAppIsOk(AppVO appVO) {
        // 应用是否存在判断
        AssertUtil.notNull(appVO, SSOLoginErrorEnum.APP_NOT_FOUND);
        // 当通过二方应用登录时，应用的回调地址必须配置
        AssertUtil.isFalse(!MoYuLoginUtil.checkIsMoYuAuthApp(appVO.getAppId())
                && StringUtils.isBlank(appVO.getRedirectUri()), SSOLoginErrorEnum.APP_CONFIG_ERROR);
        // 应用是否启用
        AssertUtil.isTrue(appVO.getEnabled(), SSOLoginErrorEnum.APP_STATE_ERROR);
    }

    /**
     * 检查应用是否是 MoYu-Auth 一方应用
     *
     * @param appId
     * @return
     */
    public static boolean checkIsMoYuAuthApp(String appId) {
        return MoYuOAuthConstant.MOYU_AUTH.equals(appId);
    }

    /**
     * 加载登录页面
     *
     * @param model 页面模型
     * @param appVO 登录应用
     */
    public static void loadLoginPage(Model model, AppVO appVO) {
        boolean allowLogin = true;
        String errorMsg = "";
        try {
            MoYuLoginUtil.checkAppIsOk(appVO);
        } catch (Exception ex) {
            // APP验证失败，禁止继续登录，返回错误信息
            allowLogin = false;
            errorMsg = ex.getMessage();
        }
        model.addAttribute(MoYuOAuthConstant.LOGIN_ERROR_MESSAGE, errorMsg);
        model.addAttribute(MoYuOAuthConstant.ALLOW_LOGIN, allowLogin);
    }

}
