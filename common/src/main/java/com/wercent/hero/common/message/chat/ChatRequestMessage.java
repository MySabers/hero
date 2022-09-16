package com.wercent.hero.common.message.chat;

import com.wercent.hero.common.message.Message;
import lombok.*;
import org.springframework.stereotype.Repository;

@Data
@Repository
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChatRequestMessage extends Message {
    private String content;
}
