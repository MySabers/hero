package com.wercent.hero.client.handler;

import com.wercent.hero.common.message.UserResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;


@Slf4j
@Controller
public class UserResponseHandler extends SimpleChannelInboundHandler<UserResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, UserResponseMessage userResponseMessage) throws Exception {
        log.info("用户登录状态信息 {}", userResponseMessage);
    }
}
