package com.ouyanglol.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ImportResource({"classpath*:**/quick-**.xml"})
@EnableScheduling
public class ScanConfig {
    public ScanConfig() {
    }
}