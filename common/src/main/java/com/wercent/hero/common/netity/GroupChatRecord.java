package com.wercent.hero.common.netity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupChatRecord {

    /**
     * 群组消息编号
     */
    private String id;

    /**
     * 组编号
     */
    private String groupId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息发送时间
     */
    private LocalDateTime sentTime;

}
