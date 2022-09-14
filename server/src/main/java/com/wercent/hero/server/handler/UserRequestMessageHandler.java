package com.wercent.hero.server.handler;

import com.wercent.hero.common.message.UserRequestMessage;
import com.wercent.hero.common.message.UserResponseMessage;
import com.wercent.hero.server.service.UserService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@ChannelHandler.Sharable
@Controller
public class UserRequestMessageHandler extends SimpleChannelInboundHandler<UserRequestMessage> {

    private final UserService service;

    public UserRequestMessageHandler(UserService service) {
        this.service = service;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserRequestMessage userRequestMessage) throws Exception {
        Map<String, String> usersState = service.getUsersState();
        UserResponseMessage message = new UserResponseMessage(usersState, true, "获取用户列表成功");
        ctx.writeAndFlush(message);
    }
}
