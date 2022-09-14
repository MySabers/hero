package com.wercent.hero.server.core;

import com.wercent.hero.common.protocol.MessageCodecSharable;
import com.wercent.hero.common.protocol.ProtocolFrameDecoder;
import com.wercent.hero.common.utils.TypeInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;

@Component
public class SocketInitializer extends ChannelInitializer<SocketChannel> {
    LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);

    final MessageCodecSharable MESSAGE_CODEC;
    final TypeInfo typeInfo;

    public SocketInitializer(MessageCodecSharable MESSAGE_CODEC, TypeInfo typeInfo) {
        this.MESSAGE_CODEC = MESSAGE_CODEC;
        this.typeInfo = typeInfo;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ProtocolFrameDecoder());
        ch.pipeline().addLast(LOGGING_HANDLER);
        ch.pipeline().addLast(MESSAGE_CODEC);
        List<Object> handles = typeInfo.getBeansWithAnnotation(Controller.class);
        for (Object handle : handles) {
            ch.pipeline().addLast((ChannelHandler) handle);
        }
    }
}
