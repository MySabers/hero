package com.wercent.hero.server.service;

import java.util.List;
import java.util.Map;

/**
 * 用户管理接口
 */
public interface UserService {

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回 true, 否则返回 false
     */
    boolean login(String username, String password);

    /**
     * 获取所有用户的上线下线状态
     * @return 用户名和状态字典
     */
    Map<String, String> getUsersState();
}
