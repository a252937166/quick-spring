package com.ouyang.quick;

import com.ouyang.annotation.QuickSpring;
import com.ouyang.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Package: com.ouyang.quick
 *
 * @Author: Ouyang
 * @Date: 2018/1/29
 */
@QuickSpring
public class MyQuick {
    @Autowired
    TestService testService;

    public void test() {
        testService.test();
        System.out.println("MyQuick");
    }
}
