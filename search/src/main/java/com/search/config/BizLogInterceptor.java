package com.search.config;


import com.alibaba.fastjson.JSONObject;
import com.search.annotation.BizLog;
import com.search.annotation.LoginConditionAnnotation;
import com.search.dao.BizLogContext;
import com.search.common.utils.R;
import com.search.entity.BizLogDto;
import com.search.entity.SysUserEntity;
import com.search.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

@Aspect
@Component
@Slf4j
@LoginConditionAnnotation
public class BizLogInterceptor implements InitializingBean {


    @Resource
    private SysUserService sysUserService;

    private static ExecutorService executorService;

    @Around("@annotation(com.search.annotation.BizLog)")
    public Object doBizLog(ProceedingJoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        BizLog bizLog = method.getAnnotation(BizLog.class);
        Object object = null;
        if (bizLog == null) {
            object = pjp.proceed();
        } else {
            try {
                object = pjp.proceed();
            } finally {
                try {
                    this.handleBizLog(bizLog, method, object, pjp);
                } catch (Exception e) {
                    log.warn("error to log", e);
                }
            }
        }
        return object;
    }

    private void handleBizLog(BizLog bizLog, Method method, Object object, ProceedingJoinPoint pjp) {
        R r = null;
        if (object instanceof R) {
            r = (R) object;
        }
        Object[] args = pjp.getArgs();
        BizLogDto operationLogDto = BizLogContext.getBizLogEnv();
        Date date = new Date();
        if (args[0] instanceof SysUserEntity && "login".equals(method.getName())) {
            SysUserEntity user = (SysUserEntity) args[0];
            operationLogDto.setGmtCreate(date);
            operationLogDto.setGmtModified(date);
            operationLogDto.setUserName(user.getUserName());
            operationLogDto.setCreatorId(user.getId());
            if (r != null && r.isSuccess()) {
                operationLogDto.setAction("登录成功");
            } else {
                operationLogDto.setAction("登录失败");
            }
        } else {
            operationLogDto.setGmtCreate(date);
            operationLogDto.setGmtModified(date);
            operationLogDto.setAction(bizLog.action());
        }
        log.info("业务日志:{}", JSONObject.toJSONString(operationLogDto));
        insertLog(operationLogDto);
    }


    public Method getMethod(ProceedingJoinPoint pjp) {
        //获取参数的类型
        Method method = null;
        try {
            Signature sig = pjp.getSignature();
            MethodSignature msig = null;
            if (!(sig instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法");
            }
            msig = (MethodSignature) sig;
            method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return method;

    }

    @Override
    public void afterPropertiesSet() {
        if (executorService == null) {
            //如果外部没有注入线程池
            executorService = new ThreadPoolExecutor(3, 3,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    Executors.defaultThreadFactory());
        }
    }

    private void insertLog(BizLogDto operationLogDto) {
        executorService.execute(() -> {
            try {
                sysUserService.addUserOperationLog(operationLogDto);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("failed to log:{}", JSONObject.toJSONString(operationLogDto));
            }finally {
                BizLogContext.clear();
            }
        });
    }
}

