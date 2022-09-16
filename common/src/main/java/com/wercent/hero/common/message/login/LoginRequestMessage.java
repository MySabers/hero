package com.wercent.hero.common.message.login;

import com.wercent.hero.common.message.Message;
import lombok.*;
import org.springframework.stereotype.Repository;


@Data
@Repository
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginRequestMessage extends Message {
    private String username;
    private String password;
}
