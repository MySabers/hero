package com.wercent.hero.server.handler;

import com.wercent.hero.server.message.LoginRequestMessage;
import com.wercent.hero.server.message.LoginResponseMessage;
import com.wercent.hero.server.service.UserService;
import com.wercent.hero.server.session.Session;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Controller;

@ChannelHandler.Sharable
@Controller
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    private final UserService userService;

    private final Session session;

    public LoginRequestMessageHandler(UserService userService, Session session) {
        this.userService = userService;
        this.session = session;
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
    }
}
