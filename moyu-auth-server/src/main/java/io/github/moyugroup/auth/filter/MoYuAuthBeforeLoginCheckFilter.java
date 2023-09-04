package io.github.moyugroup.auth.filter;

import io.github.moyugroup.auth.constant.MoYuAuthConstant;
import io.github.moyugroup.auth.pojo.vo.AppVO;
import io.github.moyugroup.auth.service.AppService;
import io.github.moyugroup.exception.AssertException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * 摸鱼登录中心
 * 登录检查过滤器
 * <p>
 * Created by fanfan on 2023/09/04.
 */
@Slf4j
public class MoYuAuthBeforeLoginCheckFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher antPathRequestMatcher;
    private final AppService appService;

    public MoYuAuthBeforeLoginCheckFilter(String filterUrl, AppService appService) {
        this.antPathRequestMatcher = new AntPathRequestMatcher(filterUrl, HttpMethod.POST.name());
        this.appService = appService;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.antPathRequestMatcher.matches(request)) {
            log.info("MoYuAuthSsoLoginCheckFilter.doFilter");
            try {
                ssoLoginParamCheck(request);
            } catch (Exception exception) {
//                request.getRequestDispatcher("/ssoLogin").forward(request, response);
                // TODO spring security 不能用内部转发
                response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 登录参数检查
     *
     * @param httpServletRequest
     */
    private void ssoLoginParamCheck(HttpServletRequest httpServletRequest) {
        String appId = httpServletRequest.getParameter(MoYuAuthConstant.APP_ID);
        if (StringUtils.isNotBlank(appId)) {
            AppVO appById = appService.getAppById(appId);
            if (Objects.isNull(appById)) {
                throw new AssertException("应用未在统一登录中心注册");
            }
        }
    }

}
