package com.ouyanglol;


import com.ouyanglol.core.QuickBase;
import com.ouyanglol.model.Test;
import com.ouyanglol.quick.MyQuick;
import com.ouyanglol.service.TestService;
import com.ouyanglol.test.TestQuick;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {

        QuickBase quickBase = QuickBase.getInstance();
        MyQuick quick = (MyQuick) quickBase.getQuick("MyQuick");
        quick.test();

        QuickBase quickBase1 = QuickBase.getInstance("test");
        TestQuick quick1 = (TestQuick) quickBase1.getQuick("TestQuick");
        quick1.test();

        TestService testService = (TestService) quickBase.getBean("testService");
        testService.test();

        testService.find(1);
        testService.find(2);
        testService.find(3);
        testService.find(4);

        Test test = new Test();
        test.setId(1);
        test.setText("无法法师打发士大夫");
        test.setT2("t2t2t2t2");
        quick1.update(test);

    }
}
