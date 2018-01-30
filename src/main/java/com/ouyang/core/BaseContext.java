package com.ouyang.core;


import com.ouyang.annotation.QuickSpring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Package: com.ouyang.core
 *
 * @Author: Ouyang
 * @Date: 2018/1/26
 */
public class BaseContext extends AnnotationConfigApplicationContext {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String RESOURCE_PATTERN = "**/%s/**/*.class";//默认扫描所有
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    protected ApplicationContext applicationContext;
    private String runPackage = "quick";
    protected Map<String,Object> startMap = new HashMap<>();

    public BaseContext() {
        logger.info("开始注册spring类...");
        register(ScanConfig.class);
        this.refresh();
        this.init();
    }


    public void init() {
        applicationContext = this;
        buildStartMap(runPackage);
    }

    private Set<Class<?>> scan(String runPackage, Class<? extends Annotation> annotationTag) {
        Set<Class<?>> resClazzSet = new HashSet<>();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + String.format(RESOURCE_PATTERN, ClassUtils.convertClassNameToResourcePath(runPackage));
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    if (ifMatchesEntityType(reader, readerFactory,new AnnotationTypeFilter(annotationTag, false))) {
                        Class<?> curClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                        resClazzSet.add(curClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resClazzSet;
    }

    /**
     * 检查当前扫描到的类是否含有任何一个指定的注解标记
     *
     * @param reader
     * @param readerFactory
     * @return ture/false
     */
    private boolean ifMatchesEntityType(MetadataReader reader, MetadataReaderFactory readerFactory, AnnotationTypeFilter typeFilter) {
        try {
            if (typeFilter.match(reader, readerFactory)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void buildStartMap(String runPackage) {
        Set<Class<?>> startClazzSet = this.scan(runPackage, QuickSpring.class);
        for (Class cls:startClazzSet){
            QuickSpring c = (QuickSpring) cls.getAnnotation(QuickSpring.class);
            String simpleName = cls.getSimpleName().substring(0,1).toLowerCase()+cls.getSimpleName().substring(1);//首字母换小写
            String name = StringUtils.isEmpty(c.name())?cls.getSimpleName():c.name();
            startMap.put(name,applicationContext.getBean(simpleName));
            logger.info("成功加载启动类->{}",name);
        }
    }

}
