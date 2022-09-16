package com.wercent.hero.server.handler;

import com.wercent.hero.common.message.login.LoginRequestMessage;
import com.wercent.hero.common.message.login.LoginResponseMessage;
import com.wercent.hero.common.message.UserResponseMessage;
import com.wercent.hero.server.broadcast.UserBroadcast;
import com.wercent.hero.server.service.UserService;
import com.wercent.hero.server.session.Session;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@ChannelHandler.Sharable
@Controller
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    private final UserService userService;

    private final Session session;

    private final UserBroadcast userBroadcast;

    public LoginRequestMessageHandler(UserService userService, Session session, UserBroadcast userBroadcast) {
        this.userService = userService;
        this.session = session;
        this.userBroadcast = userBroadcast;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = userService.login(username, password);
        LoginResponseMessage message;
        if (login) {
            session.bind(ctx.channel(), username);
            message = new LoginResponseMessage(true, "登录成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message);
        if (login) {
            UserResponseMessage userResponseMessage =
                    new UserResponseMessage(userService.getUsersState(), true, "获取用户列表成功");
            userBroadcast.userStateBroadcast(ctx.channel(), userResponseMessage);
        }
    }
}
