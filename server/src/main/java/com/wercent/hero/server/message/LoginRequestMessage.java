package com.wercent.hero.server.message;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Repository("loginRequest")
public class LoginRequestMessage extends Message {
    private String username;
    private String password;
}
