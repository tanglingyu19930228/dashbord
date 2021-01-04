package com.search.bean;

import com.search.annotation.LoginConditionAnnotation;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * 条件注入Bean
 *
 * @author tanglingyu
 */
public class LoginCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        //是否开启登录校验
        String env = environment.getProperty("checkLogin");
        Map<String, Object> map = annotatedTypeMetadata.getAnnotationAttributes(LoginConditionAnnotation.class.getName());
        Object property = map.get("checkLogin");
        if (Objects.nonNull(env) && Objects.nonNull(property)) {
            if (env.equals(property)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
}
