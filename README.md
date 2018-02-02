[TOC]

# 框架介绍

## 功能

让普通非web项目在使用`main`方法启动的时候，也能随意使用`spring`的`@Service`，`@Autowired`等语法，同时该项目整合了`mybatis`，方便了普通非web项目对数据库的操作。

## 设计思路

我在研究爬虫框架的时候，发现大多是爬虫框架都是通过`main`方法启动的，因为它们并不需要`web`容器，所以为了轻量级，都避开了与`springMVC`的整合。但是我个人习惯了`spring`的语法，同时并不喜欢原生`JdbcTemplate`的操作，更喜欢`mybatis`，所以设计了`Quick Spring`。
它使普通项目与`spring`的`spring-core`和`spring-context`两个模块简单整合，同时使用`mybatis`，方便了对数据库的操作。

## 源码地址

https://github.com/a252937166/quick-spring

# 使用介绍

## 测试项目目录结构

![这里写图片描述](http://img.blog.csdn.net/20180131173341656?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTXJfT09P/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

**图（1）**

## 导入maven

如何创建maven项目就不多介绍了，不懂得同学可以看一下[idea如何创建maven项目（一）](http://blog.csdn.net/mr_ooo/article/details/53871828)，超简单的。

### maven地址：

```
   <dependency>
      <groupId>com.ouyanglol</groupId>
      <artifactId>quick-spring</artifactId>
      <version>1.0</version>
    </dependency>
```

![这里写图片描述](http://img.blog.csdn.net/20180131173134973?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTXJfT09P/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

**图（2）**
 
## 配置文件

`Quick Spring`会扫描`resource`下以`quick-`开头的`xml`文件，默认为`spring`的配置文件，配置语法和普通`spring`配置一样，我这里提供简单模板，需要个性化配置的，可以自己改。
 
### quick-applicationContext.xml
 

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <!--选择最流行的druid连接池-->
    <bean id="mybatisDataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${database.driverClassName}"/>
        <property name="url" value="${database.url}"/>
        <property name="username" value="${database.username}"/>
        <property name="password" value="${database.password}"/>

        <!-- 初始化连接数量 -->
        <property name="initialSize" value="${druid.initialSize}" />
        <!-- 最小空闲连接数 -->
        <property name="minIdle" value="${druid.minIdle}" />
        <!-- 最大并发连接数 -->
        <property name="maxActive" value="${druid.maxActive}" />
        <!-- 配置获取连接等待超时的时间 -->
        <property name="maxWait" value="${druid.maxWait}" />

        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
        <property name="timeBetweenEvictionRunsMillis" value="${druid.timeBetweenEvictionRunsMillis}" />

        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
        <property name="minEvictableIdleTimeMillis" value="${druid.minEvictableIdleTimeMillis}" />
        <property name="validationQuery" value="${druid.validationQuery}" />
        <property name="testWhileIdle" value="${druid.testWhileIdle}" />
        <property name="testOnBorrow" value="${druid.testOnBorrow}" />
        <property name="testOnReturn" value="${druid.testOnReturn}" />

        <!-- 打开PSCache，并且指定每个连接上PSCache的大小 如果用Oracle，则把poolPreparedStatements配置为true，mysql可以配置为false。 -->
        <property name="poolPreparedStatements" value="${druid.poolPreparedStatements}" />
        <property name="maxPoolPreparedStatementPerConnectionSize"
                  value="${druid.maxPoolPreparedStatementPerConnectionSize}" />

        <!-- 配置监控统计拦截的filters -->
        <property name="filters" value="${druid.filters}" />

    </bean>

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="mybatisDataSource"/>
        <property name="mapperLocations">
            <list>
                <value>classpath:mapper/*.xml</value>
            </list>
        </property>
    </bean>

    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.ouyang.dao"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    </bean>

    <!--选择自动扫描路径-->
    <context:component-scan base-package="com.ouyang"/>
    <!--选择配置文件-->
    <context:property-placeholder location="classpath:application.properties" />
</beans>
```
其中`<context:component-scan base-package="com.ouyang"/>`，`<property name="basePackage" value="com.ouyang.dao"/>`和`<context:property-placeholder location="classpath:application.properties" />`大家根据自己项目路径自己改一下。

### log4j.properties

```
log4j.rootLogger=DEBUG,stdout,D,E,I
### 输出到控制台 ###
log4j.appender.stdout = org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target = System.out
log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss} [%t:%r:%L] - [%p] %m%n

### 输出到日志文件 ###
log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = /data/log/quick.debug.log
log4j.appender.D.Append = true
log4j.appender.D.Threshold = DEBUG
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss} [%t:%r] - [%p] %m%n

### 输出到日志文件 ###
log4j.appender.I = org.apache.log4j.DailyRollingFileAppender
log4j.appender.I.File = /data/log/quick.info.log
log4j.appender.I.Append = true
log4j.appender.I.Threshold = INFO
log4j.appender.I.layout = org.apache.log4j.PatternLayout
log4j.appender.I.layout.ConversionPattern = %-d{yyyy-MM-dd HH:mm:ss} [%t:%r] - [%p] %m%n

### 保存异常信息到单独文件 ###
log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
log4j.appender.E.File = /data/log/quick.error.log
log4j.appender.E.Append = true
log4j.appender.E.Threshold = ERROR
log4j.appender.E.layout = org.apache.log4j.PatternLayout
log4j.appender.E.layout.ConversionPattern =%-d{yyyy-MM-dd HH:mm:ss} [%t:%r] - [%p] %m%n

```

我顺便把日志也整合了，日志我用的是`slf4j`整合`log4j`，也是当下最流行的选择，使用方法很简单，等下java代码里简单介绍一下。
其中日志的输出路径，大家可以根据自己的需求修改一下。
 
### application.properties

```
database.driverClassName=com.mysql.cj.jdbc.Driver
database.url=jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8&useSSL=false
database.username=mysql
database.password=123456


druid.initialSize=10
druid.minIdle=6
druid.maxActive=50
druid.maxWait=60000
druid.timeBetweenEvictionRunsMillis=60000
druid.minEvictableIdleTimeMillis=300000
druid.validationQuery=SELECT 1
druid.testWhileIdle=true
druid.testOnBorrow=false
druid.testOnReturn=false
druid.poolPreparedStatements=false
druid.maxPoolPreparedStatementPerConnectionSize=20
druid.filters=wall,stat

```

统一记录配置信息。

## 启动类

MyQuick.java

```
package com.ouyang.quick;

import com.ouyang.service.TestService;
import com.ouyanglol.annotation.QuickSpring;
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


```
我个人建议启动类都放在`quick`的包路径下（当然也可以自定义），只需要加上`@QuickSpring`注释就行了，启动类中可以随意使用`spring`注释，`name`默认为类名，当然可以可自己修改。

TestQuick.java（自定义`name`和包路径）
从项目路径可以看到，该文件在`com.ouyang.test`包路径下。
```
package com.ouyang.test;


import com.ouyang.model.Test;
import com.ouyang.service.TestService;
import com.ouyanglol.annotation.QuickSpring;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Package: com.ouyang.test
 *
 * @Author: Ouyang
 * @Date: 2018/1/29
 */
@QuickSpring(name = "myTest")
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


```

## Server类

TestServer.java

```
package com.ouyang.service;

import com.ouyang.dao.TestMapper;
import com.ouyang.model.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    TestMapper testMapper;
    public void test() {
        
        logger.info("这是{}方法","test()");
        
        System.out.println("test Service");
    }

    public void find(Integer id) {

        logger.info("这是{}方法","find()");
        
        Test test = testMapper.selectByPrimaryKey(id);
        System.out.println(test.getText());
    }
    public void update(Test test) {
        testMapper.updateByPrimaryKeySelective(test);
    }
}

```

其中`logger`是典型的`slf4j`框架的使用方法，`testMapper`是`mybatis`的mapper，mapper的代码就没必要贴出来了，需要使用`mybatis`的自己使用工具生成就是了。
生成工具（实现中文注释）地址：https://github.com/a252937166/mybatis-generator-core-1.3.2

## main方法启动

App.java 

```
package com.ouyang;



import com.ouyang.model.Test;
import com.ouyang.quick.MyQuick;
import com.ouyang.service.TestService;
import com.ouyang.test.TestQuick;
import com.ouyanglol.core.QuickBase;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {

        QuickBase quickBase = QuickBase.getInstance();//初始化quick包下的启动类
        MyQuick quick = (MyQuick) quickBase.getQuick("MyQuick");
        quick.test();

        QuickBase quickBase1 = QuickBase.getInstance("test");//初始化test包下的启动类
        TestQuick quick1 = (TestQuick) quickBase1.getQuick("myTest");
        quick1.test();

        TestService testService = (TestService) quickBase.getBean("testService");
        testService.test();

        testService.find(1);
        testService.find(2);
        testService.find(3);
        testService.find(4);


    }
}

```
首先使用`QuickBase quickBase = QuickBase.getInstance();`初始化需要使用的启动类，如果不是在`quick`包下，需要手动填入包名。
```
quickBase.getQuick();
```
这个方法获取启动类，其中`myTest`与`@QuickSpring(name = "myTest")`中的`name`对应，不填`name`就默认是类名。获取之后就是随意调用方法了。直接启动`main`方法就能使用了。

```
quickBase.getBean("testService")
```
这个方法可以单独获取spring的bean对线。

## 测试结果

![这里写图片描述](http://img.blog.csdn.net/20180131181425275?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvTXJfT09P/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

**图(3)**

很明显，`logger`和`mybatis`都成功使用了。
