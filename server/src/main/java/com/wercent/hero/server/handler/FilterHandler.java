package com.wercent.hero.server.handler;

import com.wercent.hero.common.message.login.RegisterRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class FilterHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ((msg instanceof LoginRequestMessageHandler) || msg instanceof RegisterRequestMessage) {
            super.channelRead(ctx, msg);
        } else {

        }

    }
}
