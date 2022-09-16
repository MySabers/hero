package com.wercent.hero.common.netity;

import lombok.Builder;

@Builder
public class ChatRecord {

    /**
     * 聊天记录编号
     */
    private String id;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 发送者id
     */
    private String fromId;

    /**
     * 发送目标id
     */
    private String toId;

    /**
     * 发送时间
     */
    private String sentTime;

    /**
     * 状态 ChatRecordState
     */
    private String state;

    public void setState(ChatRecordState state) {
        this.state = state.name();
    }
}