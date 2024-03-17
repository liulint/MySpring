package com.testSpring.project.services;


import com.spring.annotation.Scope;
import com.spring.annotation.Service;


/**
 * @Author: ly
 * @Package: com.test.services
 * @Project: mySpring
 * @name: OneService
 * @Date:2024/1/7 17:16
 */
@Service("wewefwef")
@Scope("prototype")
public class OneService {

    public void test(){
//        AnnotationInvocationHandler annotationInvocationHandler = new AnnotationInvocationHandler();
//        Service annotation = OneService.class.getAnnotation(Service.class);
//        Component annotation2 = AnnotationUtil.findMergedAnnotation(OneService.class, Component.class);
//        String value = annotation2.value();
//        Component annotation = AnnotatedElementUtils.findMergedAnnotation(OneService.class, Component.class);
        System.out.println("OneService test");
    }

    public String test1(){
        return "OneService test1方法";
    }

//    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
//        test();
//    }
}
