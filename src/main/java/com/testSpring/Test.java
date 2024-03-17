package com.testSpring;

import com.spring.MyApplicationContext;
import com.testSpring.project.App;
import com.testSpring.project.services.OneService;
import com.testSpring.project.services.TwoService;

/**
 * @Author: ly
 * @Package: com.test
 * @Project: mySpring
 * @name: Test
 * @Date:2024/1/7 16:00
 */
public class Test {
    public static void main(String[] args) {
        MyApplicationContext myApplicationContext = new MyApplicationContext(SpringConfig.class);
        App app = (App) myApplicationContext.getBean("app");
        app.test();
        OneService oneService = (OneService) myApplicationContext.getBean("wewefwef");
        oneService.test();
        OneService oneService1 = (OneService) myApplicationContext.getBean("wewefwef");
        oneService1.test();
        TwoService twoService = (TwoService) myApplicationContext.getBean("twoService");
        twoService.test();
    }
}
