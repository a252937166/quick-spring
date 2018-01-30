package com.ouyang.test;

import com.ouyang.annotation.QuickSpring;
import com.ouyang.model.Test;
import com.ouyang.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Package: com.ouyang.test
 *
 * @Author: Ouyang
 * @Date: 2018/1/29
 */
@QuickSpring
public class TestQuick {
    @Autowired
    TestService testService;

    public void  test() {
        testService.test();
        System.out.println("TestQuick");
    }

    public void update(Test test) {
        testService.update(test);
    }
}
