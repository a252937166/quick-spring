package com.ouyanglol.service;

import com.ouyanglol.dao.TestMapper;
import com.ouyanglol.model.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Package: com.ouyang.service
 *
 * @Author: Ouyang
 * @Date: 2018/1/26
 */
@Service
public class TestService {
    @Autowired
    TestMapper testMapper;
    public void test() {
        System.out.println("test Service");
    }

    public void find(Integer id) {
        Test test = testMapper.selectByPrimaryKey(id);
        System.out.println(test.getText());
    }

    public void update(Test test) {
        testMapper.updateByPrimaryKeySelective(test);
    }
}
