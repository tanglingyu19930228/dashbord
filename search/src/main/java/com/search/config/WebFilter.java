package com.search.config;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheLoader;
import com.search.dao.BizLogContext;
import com.search.entity.BizLogDto;
import com.search.common.domain.BusinessResponse;
import com.search.common.domain.BusinessResponseEnum;
import com.search.common.utils.StringUtils;
import com.search.entity.SysUserEntity;
import com.search.service.impl.SysUserServiceImpl;
import io.undertow.servlet.spec.HttpServletRequestImpl;
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
import java.util.Objects;

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
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        try {
            String contextPath = ((HttpServletRequestImpl) request).getExchange().getRequestURL();
            String loginToken = getFromHeaderOrCookie(request, "login_token");
            if (contextPath.contains("/sysUser/login")) {
                //pay attention to
                if (StringUtils.isNotEmpty(loginToken)) {
                    SysUserEntity sysUserEntity = resolveLoginUser(loginToken);
                    if (Objects.isNull(sysUserEntity)) {
                        return;
                    } else {
                        setBizLogEnv(sysUserEntity, request);
                    }
                } else {
                    BizLogDto bizLogEnv = new BizLogDto();
                    bizLogEnv.setLoginIp(getIpAddr(request));
                    BizLogContext.setEnv(bizLogEnv);
                }
            } else {
                if (StringUtils.isNotEmpty(loginToken)) {
                    SysUserEntity sysUserEntity = resolveLoginUser(loginToken);
                    //ThreadLocal设置操作日志上下文
                    if (Objects.nonNull(sysUserEntity)) {
                        setBizLogEnv(sysUserEntity, request);
                    } else {
                        //正常情况是走不到这里的--resolveLoginUser会抛出异常--逻辑严谨性login_token不对
                        return;
                    }
                }
            }
        } catch (Exception e) {
            BusinessResponse wr = new BusinessResponse();
            wr.setCode(BusinessResponseEnum.SYSTEM_ERROR.getCode());
            if (e instanceof CacheLoader.InvalidCacheLoadException) {
                wr.setMsg("登录token不对");
            } else {
                wr.setMsg(e.getMessage());
            }
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(200);
            httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "true");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "*");
//            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept ,token");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
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

    private SysUserEntity resolveLoginUser(String loginToken) throws Exception {
        String token = String.format("LOGIN_TOKEN_%s", loginToken);
        SysUserEntity result = sysUserService.resolveUserByToken(token);
        if (result != null) {
            RequestContextHolder.getRequestAttributes().setAttribute(KEY, result, RequestAttributes.SCOPE_REQUEST);
            return result;
        }
        return null;
    }


    private void setBizLogEnv(SysUserEntity sysUserEntity, HttpServletRequest request) {
        BizLogDto bizLogEnv = new BizLogDto();
        bizLogEnv.setCreatorId(sysUserEntity.getId());
        bizLogEnv.setUserName(sysUserEntity.getUserName());
        bizLogEnv.setLoginIp(getIpAddr(request));
        BizLogContext.setEnv(bizLogEnv);
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
