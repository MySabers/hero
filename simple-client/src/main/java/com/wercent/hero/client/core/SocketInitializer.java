package com.wercent.hero.client.core;

import com.wercent.hero.client.gui.Login;
import com.wercent.hero.client.handler.ClientChannelDuplexHandler;
import com.wercent.hero.client.handler.LoginResponseHandler;
import com.wercent.hero.common.protocol.MessageCodecSharable;
import com.wercent.hero.common.protocol.ProtocolFrameDecoder;
import com.wercent.hero.common.utils.TypeInfo;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class SocketInitializer extends ChannelInitializer<SocketChannel> {
    LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    private final LoginResponseHandler LOGIN_RESPONSE_HANDLER;

    private final MessageCodecSharable MESSAGE_CODEC;
    private final TypeInfo typeInfo;

    private final Login loginFrame;

    public SocketInitializer(MessageCodecSharable MESSAGE_CODEC,
                             TypeInfo typeInfo, Login loginFrame, LoginResponseHandler LOGIN_RESPONSE_HANDLER) {
        this.MESSAGE_CODEC = MESSAGE_CODEC;
        this.typeInfo = typeInfo;
        this.loginFrame = loginFrame;
        this.LOGIN_RESPONSE_HANDLER = LOGIN_RESPONSE_HANDLER;
    }
    AtomicBoolean EXIT = new AtomicBoolean(false);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ch.pipeline().addLast(new ProtocolFrameDecoder());
        ch.pipeline().addLast(LOGGING_HANDLER);
        ch.pipeline().addLast(MESSAGE_CODEC);
        // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
        // 3s 内如果没有向服务器写数据，会触发一个 IdleState#WRITER_IDLE 事件
        ch.pipeline().addLast(new IdleStateHandler(0, 3, 0));
        // ChannelDuplexHandler 可以同时作为入站和出站处理器
        ch.pipeline().addLast(new ClientChannelDuplexHandler());
        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                loginFrame.process(ch);
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                log.debug("连接已经断开，按任意键退出..");
                EXIT.set(true);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                log.debug("连接已经断开，按任意键退出..{}", cause.getMessage());
                EXIT.set(true);
            }
        });
        ch.pipeline().addLast(LOGIN_RESPONSE_HANDLER);

    }
}
