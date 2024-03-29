package io.github.moyugroup.auth.demo.controller;

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
//        model.addAttribute("name", Objects.isNull(authentication) ? "" : authentication.getName());
//        model.addAttribute("sessionId", session.getId());
        return "index";
    }
}
