package com.wercent.hero.server;

import com.wercent.hero.server.message.LoginRequestMessage;
import com.wercent.hero.server.message.MessageWrap;
import com.wercent.hero.server.protocol.MessageCodecSharable;
import com.wercent.hero.server.protocol.ProtocolFrameDecoder;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
public class ServerApplicationTest {

    @Resource
    MessageCodecSharable MESSAGE_CODEC;

    @Test
    public void main() {

        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("aaaa");
                super.channelRead(ctx, msg);
            }
        };

        ChannelOutboundHandlerAdapter h2 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug(msg.toString());
                super.write(ctx, msg, promise);
            }
        };


        EmbeddedChannel channel = new EmbeddedChannel(MESSAGE_CODEC);
        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("asdsad", "sadsadasd");
        channel.writeInbound(loginRequestMessage);
    }
}

