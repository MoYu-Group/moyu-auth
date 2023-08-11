package io.github.moyugroup.auth.demo.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;

/**
 * 自定义登录过程
 * <p>
 * Created by fanfan on 2023/08/11.
 */
public class MoYuClientAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = request.getParameter("username");
        username = (username != null) ? username.trim() : "";
        String password = request.getParameter("password");
        password = (password != null) ? password : "";
        // 创建认证通过的对象，目前免认证登录，所有账密都可以登录，后续会调用 MoYu-Auth 认证登录
        User user = new User(username, password, new ArrayList<>());
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, password, new ArrayList<>());
        setDetails(request, authenticated);
        return authenticated;
    }

}
