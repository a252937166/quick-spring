package com.ouyanglol.test;

import com.ouyanglol.annotation.QuickSpring;
import com.ouyanglol.model.Test;
import com.ouyanglol.service.TestService;
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
