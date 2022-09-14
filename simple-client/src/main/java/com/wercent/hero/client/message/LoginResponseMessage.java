package com.wercent.hero.client.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Data
@ToString(callSuper = true)
@Repository
@NoArgsConstructor
public class LoginResponseMessage extends AbstractResponseMessage {
    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
}
