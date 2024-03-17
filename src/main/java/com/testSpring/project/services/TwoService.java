package com.testSpring.project.services;

import com.spring.annotation.Autowired;
import com.spring.annotation.Qualifier;
import com.spring.annotation.Service;

/**
 * @Author: ly
 * @Package: com.test.services
 * @Project: mySpring
 * @name: TwoService
 * @Date:2024/1/7 17:20
 */
@Service
public class TwoService {
    @Autowired
    @Qualifier("wewefwef")
    OneService oneServices;
    public void test(){
        System.out.println("TwoService test"+oneServices);
    }
}
