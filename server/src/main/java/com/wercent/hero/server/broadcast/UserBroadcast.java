package com.wercent.hero.server.broadcast;

import com.wercent.hero.common.message.Message;
import com.wercent.hero.common.message.UserResponseMessage;
import com.wercent.hero.server.service.UserService;
import com.wercent.hero.server.session.Session;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;


/**
 * 用户状态广播，包括登录，异常退出等
 */
@Component
public class UserBroadcast {

    private final Session session;


    public UserBroadcast(Session session) {
        this.session = session;
    }

    public void userStateBroadcast(Channel currentChannel, Message message) {
        for (String name : session.getNames()) {
            Channel channel = session.getChannel(name);
            if (channel != currentChannel) {
                channel.writeAndFlush(message);
            }
        }
    }

    public void chatBroadcast(Message message) {
        for (String name : session.getNames()) {
            Channel channel = session.getChannel(name);
            channel.writeAndFlush(message);
        }
    }
}
