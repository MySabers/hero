package com.wercent.hero.server.handler;

import com.wercent.hero.common.message.UserResponseMessage;
import com.wercent.hero.server.service.UserService;
import com.wercent.hero.server.session.Session;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@ChannelHandler.Sharable
@Controller
public class QuitHandler extends ChannelInboundHandlerAdapter {
    private final Session session;

    private final UserService service;

    public QuitHandler(Session session, UserService service) {
        this.session = session;
        this.service = service;
    }

    // 当连接断开时触发 inactive 事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        session.unbind(ctx.channel());
        log.debug("{} 已经断开", ctx.channel());
        // 更新客户端登录信息列表
        UserResponseMessage message =
                new UserResponseMessage(service.getUsersState(), true, "获取用户列表成功");
        ctx.writeAndFlush(message);
    }

    // 当出现异常时触发
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        session.unbind(ctx.channel());
        UserResponseMessage message =
                new UserResponseMessage(service.getUsersState(), true, "获取用户列表成功");
        ctx.writeAndFlush(message);
        log.debug("{} 已经异常断开 异常是{}", ctx.channel(), cause.getMessage());
    }
}
