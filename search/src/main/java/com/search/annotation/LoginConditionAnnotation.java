package com.search.annotation;

import com.search.bean.LoginCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author tanglingyu
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(LoginCondition.class)
public @interface LoginConditionAnnotation {

    //默认校验登录
    String checkLogin() default "true";
}
