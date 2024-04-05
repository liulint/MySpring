package com.testSpring.project.services;


import com.spring.annotation.Component;
import com.spring.annotation.Scope;
import com.testSpring.project.TestInterface;

/**
 * @Author: ly
 * @Package: com.test.services
 * @Project: mySpring
 * @name: OneService
 * @Date:2024/1/7 17:16
 */
@Scope("protoType")
@Component
public class OneService implements TestInterface {



    public String test1(){
        return "OneService test1方法";
    }


    @Override
    public void test() {
        System.out.println("OneService");

    }
}
