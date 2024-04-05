package com.spring.lifeStyle;

public interface BeanPostProcessor {

    //初始化前执行
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }
    //初始化后执行
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
