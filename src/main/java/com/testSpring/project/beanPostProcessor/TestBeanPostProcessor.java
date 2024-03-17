package com.testSpring.project.beanPostProcessor;

import com.spring.annotation.Component;
import com.spring.lifeStyle.BeanPostProcessor;

/**
 * @Author: ly
 * @Package: com.test.project.beanPostProcessor
 * @Project: mySpring
 * @name: TestBeanPostProcessor
 * @Date:2024/1/14 16:51
 */
@Component
public class TestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

        System.out.println("实例化前1"+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("实例化后1"+beanName);
        return bean;
    }
}
