package com.ouyanglol.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface QuickSpring {
    /**
     * 自定义启动类名字，默认类名
     */
    String name() default "";

}