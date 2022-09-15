package com.wercent.hero.common.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@Repository
public class ChatResponseMessage  extends AbstractResponseMessage {

    private String from;
    private String content;

    public ChatResponseMessage(String from, String content, boolean success, String reason) {
        super(success, reason);
        this.from = from;
        this.content = content;
    }
}
