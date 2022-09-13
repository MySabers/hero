package com.wercent.hero.message;

public class MessageWrap<T> {
    private int sequenceId;
    private String messageType;
    private T body;
}
