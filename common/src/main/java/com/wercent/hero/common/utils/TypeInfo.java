package com.wercent.hero.common.utils;

import java.lang.annotation.Annotation;
import java.util.List;

public interface TypeInfo {

    /**
     * 根据类型获取名称
     * @param type class类型
     * @return 类型对应的名称
     */
    String getNameByType(Class<?> type);

    /**
     * 根据名称获取对应的类型
     * @param name 名称
     * @return 名称对应的类名
     */
    Class<?> getTypeByName(String name);

    /**
     * 根据注解名，获取注解类型获取所有实例
     * @param anno 注解类型
     * @return 注解实例列表
     */
    List<Object> getBeansWithAnnotation(Class<? extends Annotation> anno);
}
