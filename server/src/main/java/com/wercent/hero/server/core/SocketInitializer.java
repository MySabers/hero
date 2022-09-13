package com.wercent.hero.server.core;

import com.wercent.hero.server.protocol.MessageCodecSharable;
import com.wercent.hero.server.protocol.ProtocolFrameDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SocketInitializer extends ChannelInitializer<SocketChannel> {
    LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);

    @Resource
    MessageCodecSharable MESSAGE_CODEC;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ProtocolFrameDecoder());
        ch.pipeline().addLast(LOGGING_HANDLER);
        ch.pipeline().addLast(MESSAGE_CODEC);
    }
}
