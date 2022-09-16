package com.wercent.hero.common.netity;

import lombok.Data;

@Data
public class UserGroupChartRelation {

    /**
     * 编号
     */
    private String id;

    /**
     * 组消息编号
     */
    private String groupChatId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 状态
     */
    private String state;

}
