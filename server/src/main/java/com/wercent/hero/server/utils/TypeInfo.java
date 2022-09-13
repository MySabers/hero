package com.wercent.hero.server.utils;

public interface TypeInfo {

    String getNameByType(Class<?> type);

    Class<?> getTypeByName(String name);

}
