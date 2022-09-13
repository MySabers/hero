package com.wercent.hero.handler;


import com.wercent.hero.message.LoginRequestMessage;
import com.wercent.hero.message.LoginResponseMessage;
import com.wercent.hero.service.UserService;
//import com.wercent.hero.service.UserServiceFactory;
import com.wercent.hero.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    @Autowired
    private UserService userService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
//        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message;
        if (true) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            message = new LoginResponseMessage(true, "登录成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message);
    }
}
