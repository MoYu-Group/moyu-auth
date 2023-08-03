package io.github.moyugroup.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

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
     * @param authentication
     * @return
     */
    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("name", Objects.isNull(authentication) ? "" : authentication.getName());
        return "index";
    }
}
