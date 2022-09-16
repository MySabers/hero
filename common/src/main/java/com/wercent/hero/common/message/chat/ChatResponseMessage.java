package com.wercent.hero.common.message.chat;

import com.wercent.hero.common.message.AbstractResponseMessage;
import lombok.*;
import org.springframework.stereotype.Repository;

@Data
@Repository
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChatResponseMessage  extends AbstractResponseMessage {

    private String from;
    private String content;

    public ChatResponseMessage(String from, String content, boolean success, String reason) {
        super(success, reason);
        this.from = from;
        this.content = content;
    }
}
