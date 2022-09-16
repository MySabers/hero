package com.wercent.hero.common.message.login;

import com.wercent.hero.common.message.AbstractResponseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;


@Data
@NoArgsConstructor
@Repository
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {

    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }


}
