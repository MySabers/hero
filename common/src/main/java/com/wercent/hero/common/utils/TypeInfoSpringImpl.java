package com.wercent.hero.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

@Component
public class TypeInfoSpringImpl implements TypeInfo {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public String getNameByType(Class<?> type) {
        return applicationContext.getBeanNamesForType(type)[0];
    }

    @Override
    public Class<?> getTypeByName(String name) {
        return applicationContext.getType(name);
    }

    @Override
    public List<Object> getBeansWithAnnotation(Class<? extends Annotation> anno) {
        return Arrays.asList(applicationContext.getBeansWithAnnotation(anno).values().toArray());
    }

}
