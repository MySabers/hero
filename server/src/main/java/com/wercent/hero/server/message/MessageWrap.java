package com.wercent.hero.server.message;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageWrap {
    private int sequenceId;
    private String messageType;
    private Object body;
}
