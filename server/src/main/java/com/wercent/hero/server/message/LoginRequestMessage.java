package com.wercent.hero.server.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@Component("loginRequest")
public class LoginRequestMessage extends Message {
    private String username;
    private String password;
}
