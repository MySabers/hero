package com.wercent.hero.common.netity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户编号
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headerImg;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 签名
     */
    private String signature;
}
