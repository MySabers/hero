package com.wercent.hero.client.handler;

import com.wercent.hero.client.gui.Login;
import com.wercent.hero.common.message.login.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Controller;

@Controller
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponseMessage> {

    private final Login loginFrame;

    public LoginResponseHandler(Login loginFrame) {
        this.loginFrame = loginFrame;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginResponseMessage loginResponseMessage) throws Exception {
        if (loginResponseMessage.isSuccess()) {
            // 如果登录成功
            loginFrame.LOGIN.set(true);
        }
        // 唤醒 system in 线程
        loginFrame.WAIT_FOR_LOGIN.countDown();
    }

}
