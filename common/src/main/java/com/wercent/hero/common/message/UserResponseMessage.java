package com.wercent.hero.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Repository
public class UserResponseMessage extends AbstractResponseMessage {
    private Map<String, String> users;

    public UserResponseMessage(Map<String, String> users, boolean success, String reason) {
        super(success, reason);
        this.users = users;
    }
}
