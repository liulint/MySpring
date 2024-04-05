package com.spring.annotation;

import java.lang.annotation.*;

/**
 * @Author: ly
 * @Package: com.spring.annotation
 * @Project: mySpring
 * @name: Qualifier
 * @Date:2024/1/27 16:43
 * 指定beanName的名字，获取指定bean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
public @interface Qualifier {
    String value() default "";
}

