package com.spring.annotation;

import java.lang.annotation.*;

/**
 * @Author: ly
 * @Package: com.spring.annotation
 * @Project: mySpring
 * @name: Service
 * @Date:2024/1/13 15:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Service{
    @AliasFor(annotation = Component.class)
    String value() default "";
}
