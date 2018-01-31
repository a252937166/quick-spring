package com.ouyanglol.core;


/**
 *
 * @Author: Ouyang
 * @Date: 2018/1/26
 */

public class QuickBase extends BaseContext {

    private static QuickBase instance;

    private QuickBase() {
        super();
    }

    /**
     * 使用单例模式，避免反复注册
     * @return QuickBase
     */
    public static QuickBase getInstance() {
        if (instance == null) {
            instance = new QuickBase();
        }
        return instance;
    }

    /**
     * 自定义启动类所在包路径
     * @return QuickBase
     */
    public static QuickBase getInstance(String runPackage) {
        if (instance == null) {
            instance = new QuickBase();
        }
        instance.buildStartMap(runPackage);
        return instance;
    }

    /**
     * 获取一个启动类
     * @param name 启动类名
     * @return
     */
    public Object getQuick(String name) {
        return startMap.get(name);
    }

    /**
     * 获取一个spring的bean
     * @param name bean的名字，默认是类名，首字母小写
     * @return
     */
    public Object get(String name) {
        return applicationContext.getBean(name);
    }
}
