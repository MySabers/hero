package com.wercent.hero.server.service;

public interface UserStateService {

    /**
     * 用户上线, 存储 userId 于 ip 的关系
     * @param userId 用户编号
     * @param ip 用户登录的ip地址
     */
    void online(String userId, String ip);

    /**
     * 用户下线, 删除相关信息
     * @param userId 用户编号
     */
    void offline(String userId);

    /**
     * 根据用户id获取用户的ip
     * @param userId 用户编号
     * @return 用户所在机器ip
     */
    String getUserConnectIp(String userId);

}
