package com.spring.annotation;

import java.lang.annotation.*;

/**
 * @Author: ly
 * @Package: com.spring.annotation
 * @Project: mySpring
 * @name: Component
 * @Date:2024/1/7 16:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Component{
    String value() default "";
}
