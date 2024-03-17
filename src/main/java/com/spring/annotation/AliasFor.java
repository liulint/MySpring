package com.spring.annotation;

import java.lang.annotation.*;

/**
 * @Author: ly
 * @Package: com.spring.annotation
 * @Project: mySpring
 * @name: AliasFor
 * @Date:2024/1/14 22:47
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AliasFor {
    @AliasFor("attribute")
    String value() default "";

    @AliasFor("value")
    String attribute() default "";

    Class<? extends Annotation> annotation() default Annotation.class;
}
