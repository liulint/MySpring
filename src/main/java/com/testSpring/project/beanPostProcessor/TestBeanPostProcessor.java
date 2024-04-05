package com.testSpring.project.beanPostProcessor;

import com.spring.annotation.Component;
import com.spring.lifeStyle.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

        System.out.println("实例化前"+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("实例化后"+beanName);
//        Object proxyInstance = Proxy.newProxyInstance(TestBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                System.out.println("我是代理");
//                return method.invoke(bean,args);
//            }
//        });
        return bean;
    }
}
