package com.search.config;

import com.alibaba.fastjson.JSONObject;
import com.search.annotation.NoNeedLogin;
import com.search.common.domain.BusinessException;
import com.search.common.domain.BusinessResponse;
import com.search.common.domain.BusinessResponseEnum;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author tanglingyu
 */
@Component
public class LoginInterceptor extends HttpSessionHandshakeInterceptor implements HandlerInterceptor {

    /**
     * 拦截器对controller鉴权
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            Method targetMethod = ((HandlerMethod) handler).getMethod();
            //不需要登录的不校验权限
            if (targetMethod.getAnnotation(NoNeedLogin.class) != null || targetMethod.getDeclaringClass().getAnnotation(NoNeedLogin.class) != null) {
                return true;
            }
            Object user = RequestContextHolder.getRequestAttributes().getAttribute(WebFilter.KEY, RequestAttributes.SCOPE_REQUEST);
            if (user == null) {
                BusinessResponse wr = new BusinessResponse();
                wr.setCode(BusinessResponseEnum.NON_LOGIN.getCode());
                wr.setMsg(BusinessResponseEnum.NON_LOGIN.getMsg());
                response.setStatus(200);
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getOutputStream().write(JSONObject.toJSONString(wr).getBytes(StandardCharsets.UTF_8));
                response.getOutputStream().flush();
                return false;
            }
            return true;
        }
        return true;
    }


    /**
     * webSocket鉴权
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map attributes) throws Exception {
        Object user = RequestContextHolder.getRequestAttributes()
                .getAttribute(WebFilter.KEY, RequestAttributes.SCOPE_REQUEST);
        if (user == null) {//没有登录，无权限
            throw new BusinessException(BusinessResponseEnum.NON_LOGIN.getCode(), BusinessResponseEnum.NON_LOGIN.getMsg());
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
