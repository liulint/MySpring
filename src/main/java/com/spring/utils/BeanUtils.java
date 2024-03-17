package com.spring.utils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: ly
 * @Package: com.spring.utils
 * @Project: mySpring
 * @name: BeanUtils
 * @Date:2024/1/13 17:54
 */
public class BeanUtils {
    public static String getBeanNameByClassFile(Class<?> classFile, Class<? extends Annotation> annotationClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String beanName = "";
        Annotation annotation = classFile.getAnnotation(annotationClass);
        Method valueMethod = annotationClass.getDeclaredMethod("value");
        beanName = (String) valueMethod.invoke(annotation);
        if ("".equals(beanName)) {
            beanName = Introspector.decapitalize(classFile.getSimpleName());
        }
        return beanName;
    }
}
