package com.search.config;

import com.alibaba.fastjson.JSONObject;
import com.search.common.domain.BusinessResponse;
import com.search.common.domain.BusinessResponseEnum;
import com.search.common.utils.StringUtils;
import com.search.entity.SysUserEntity;
import com.search.service.impl.SysUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author tanglingyu
 */
@Slf4j
public class WebFilter implements Filter {

    @Resource
    private SysUserServiceImpl sysUserService;

    public static final String KEY = "CURRENT_USER";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("WebFilter init...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String loginToken = getFromHeaderOrCookie(request, "login_token");
            if (StringUtils.isNotEmpty(loginToken)) {
                resolveLoginUser(loginToken);
            }
        } catch (Exception e) {
            BusinessResponse wr = new BusinessResponse();
            wr.setCode(BusinessResponseEnum.SYSTEM_ERROR.getCode());
            wr.setMsg(e.getMessage());
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(200);
            httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpServletResponse.getOutputStream().write(JSONObject.toJSONString(wr).getBytes(StandardCharsets.UTF_8));
            httpServletResponse.getOutputStream().flush();
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("WebFilter destroy...");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("WebFilter postConstruct");
    }

    private String getFromHeaderOrCookie(HttpServletRequest request, String key) {
        String value = request.getHeader(key);
        if (StringUtils.isEmpty(value)) {
            Cookie valueCookie = WebUtils.getCookie(request, key);
            if (valueCookie != null) {
                value = valueCookie.getValue();
            }
        }
        return value;
    }

    /**
     * 通过http头里面获取用户信息
     */

    private void resolveLoginUser(String loginToken) throws Exception {
        String token = String.format("LOGIN_TOKEN_%s", loginToken);
        SysUserEntity result = sysUserService.resolveUserByToken(token);
        if (result != null) {
            RequestContextHolder.getRequestAttributes().setAttribute(KEY, result, RequestAttributes.SCOPE_REQUEST);
        }
    }
}
