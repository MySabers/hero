package com.wercent.hero.server.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

}
