package com.spring.utils;

import com.spring.annotation.AliasFor;
import com.spring.annotation.Component;
import com.spring.invocationHandler.MySynthesizedAnnotationInvocationHandler;
import com.spring.invocationHandler.SynthesizedAnnotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @Author: ly
 * @Package: com.spring.utils
 * @Project: mySpring
 * @name: AnnotationUtil
 * @Date:2024/1/20 18:12
 */
public class AnnotationUtil {

    public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, Class<A> annotationType) throws InvocationTargetException, IllegalAccessException {
        //获取该类所有注解
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            //如果类型和需要的类型一致，即可返回
            if(annotation.annotationType() == annotationType){
                return (A) annotation;
            }
            //获取该注解上自定义的方法
            Method[] declaredMethods = annotation.annotationType().getDeclaredMethods();
            for (Method method : declaredMethods) {
                method.setAccessible(true);
                //获取该方法的返回值
                Object attributeValue = method.invoke(annotation);
                AliasFor aliasFor = method.getAnnotation(AliasFor.class);
                if (!ObjectUtils.isEmpty(aliasFor) && aliasFor.annotation() == annotationType) {
                    //使用代理的方式，创建一个annotationType类型的注解返回
                    MySynthesizedAnnotationInvocationHandler handler = new MySynthesizedAnnotationInvocationHandler();
                    handler.attributeExtractor.put(method.getName(),attributeValue);
                    Class<?>[] exposedInterfaces = new Class<?>[] {annotationType, SynthesizedAnnotation.class};
                    return (A) Proxy.newProxyInstance(annotationType.getClassLoader(), exposedInterfaces, handler);
                }

            }
        }
        return null;
    }


}
