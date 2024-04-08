package io.github.moyugroup.auth.demo.controller;

import io.github.moyugroup.auth.common.context.UserContext;
import io.github.moyugroup.auth.common.pojo.dto.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页渲染
 * <p>
 * Created by fanfan on 2023/08/03.
 */
@Controller
public class IndexController {

    /**
     * 首页页面
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model) {
        UserInfo userInfo = UserContext.get();
        model.addAttribute("name", userInfo.getNickname());
        model.addAttribute("user", userInfo);
        return "index";
    }
}
