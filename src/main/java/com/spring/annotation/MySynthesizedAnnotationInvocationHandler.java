package com.spring.annotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ly
 * @Package: com.spring.annotation
 * @Project: mySpring
 * @name: MySynthesizedAnnotationInvocationHandler
 * @Date:2024/1/21 22:54
 */
public class MySynthesizedAnnotationInvocationHandler implements InvocationHandler {
    public Map<String,Object> attributeExtractor = new HashMap<>();
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return attributeExtractor.get(method.getName());
    }
}
