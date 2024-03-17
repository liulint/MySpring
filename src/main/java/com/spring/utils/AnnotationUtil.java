package com.spring.utils;

import com.spring.annotation.AliasFor;
import com.spring.annotation.MySynthesizedAnnotationInvocationHandler;
import com.spring.annotation.SynthesizedAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
