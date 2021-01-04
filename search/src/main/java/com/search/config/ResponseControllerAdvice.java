package com.search.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * @author tanglingyu
 */
//@RestControllerAdvice
//public class ResponseControllerAdvice implements ResponseBodyAdvice {
//    @Override
//    public boolean supports(MethodParameter methodParameter, Class aClass) {
//        Method method = methodParameter.getMethod();
//        return method.getAnnotation(ResponseBody.class) != null || method.getDeclaringClass().isAnnotationPresent(RestController.class);
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//        return o;
//    }
//}
