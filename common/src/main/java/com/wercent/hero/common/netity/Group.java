package com.wercent.hero.common.netity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    /**
     * 组编号
     */
    private String id;

    /**
     * 组名称
     */
    private String name;

    /**
     * 组创建用户
     */
    private String createUser;

}
