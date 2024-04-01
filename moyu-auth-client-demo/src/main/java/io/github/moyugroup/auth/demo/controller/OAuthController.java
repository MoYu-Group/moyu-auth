package io.github.moyugroup.auth.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * todo
 * <p>
 * Created by fanfan on 2024/04/01.
 */
@Controller
public class OAuthController {

    @ResponseBody
    @GetMapping("oauth2")
    public void oauth2(HttpServletRequest request) {
        String queryString = request.getQueryString();
        System.out.println(queryString);
    }
}
