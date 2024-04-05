package io.github.moyugroup.auth.web.page;

import io.github.moyugroup.auth.common.constant.SSOLoginConstant;
import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import io.github.moyugroup.auth.common.util.CookieUtil;
import io.github.moyugroup.auth.constant.MoYuOAuthConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页渲染
 * <p>
 * Created by fanfan on 2023/08/03.
 */
@Controller
public class IndexPageController {

    /**
     * 首页页面
     *
     * @param model
     * @return
     */
    @GetMapping(SSOLoginConstant.INDEX_PAGE_PATH)
    public String index(Model model, HttpServletRequest request) {
        UserInfo userInfo = UserContext.get();
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("sessionId", CookieUtil.getSSOLoginSessionId(request, MoYuOAuthConstant.MOYU_AUTH));
        return "index";
    }
}
