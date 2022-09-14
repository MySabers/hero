package com.wercent.hero.client.message;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Message implements Serializable {
    private int sequenceId;
}
