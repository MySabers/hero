package com.wercent.hero.client.core;

import com.wercent.hero.client.message.LoginRequestMessage;
import com.wercent.hero.client.message.LoginResponseMessage;
import com.wercent.hero.client.message.PingMessage;
import com.wercent.hero.client.protocol.MessageCodecSharable;
import com.wercent.hero.client.protocol.ProtocolFrameDecoder;
import com.wercent.hero.client.utils.TypeInfo;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class SocketInitializer extends ChannelInitializer<SocketChannel> {
    LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);

    final MessageCodecSharable MESSAGE_CODEC;
    final TypeInfo typeInfo;

    public SocketInitializer(MessageCodecSharable MESSAGE_CODEC, TypeInfo typeInfo) {
        this.MESSAGE_CODEC = MESSAGE_CODEC;
        this.typeInfo = typeInfo;
    }

    CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
    AtomicBoolean LOGIN = new AtomicBoolean(false);
    AtomicBoolean EXIT = new AtomicBoolean(false);
    Scanner scanner = new Scanner(System.in);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        System.out.println(MESSAGE_CODEC);
        ch.pipeline().addLast(new ProtocolFrameDecoder());
        ch.pipeline().addLast(LOGGING_HANDLER);
        ch.pipeline().addLast(MESSAGE_CODEC);
        // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
        // 3s 内如果没有向服务器写数据，会触发一个 IdleState#WRITER_IDLE 事件
        ch.pipeline().addLast(new IdleStateHandler(0, 3, 0));
        // ChannelDuplexHandler 可以同时作为入站和出站处理器
        ch.pipeline().addLast(new ChannelDuplexHandler() {
            // 用来触发特殊事件
            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
                IdleStateEvent event = (IdleStateEvent) evt;
                // 触发了写空闲事件
                if (event.state() == IdleState.WRITER_IDLE) {
//                                log.debug("3s 没有写数据了，发送一个心跳包");
                    ctx.writeAndFlush(new PingMessage());
                }
            }
        });
        ch.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter() {
            // 接收响应消息
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("msg: {}", msg);
                if ((msg instanceof LoginResponseMessage)) {
                    LoginResponseMessage response = (LoginResponseMessage) msg;
                    if (response.isSuccess()) {
                        // 如果登录成功
                        LOGIN.set(true);
                    }
                    // 唤醒 system in 线程
                    WAIT_FOR_LOGIN.countDown();
                }
            }

            // 在连接建立后触发 active 事件
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                // 负责接收用户在控制台的输入，负责向服务器发送各种消息
                new Thread(() -> {
                    System.out.println("请输入用户名:");
                    String username = scanner.nextLine();
                    if(EXIT.get()){
                        return;
                    }
                    System.out.println("请输入密码:");
                    String password = scanner.nextLine();
                    if(EXIT.get()){
                        return;
                    }
                    // 构造消息对象
                    LoginRequestMessage message = new LoginRequestMessage(username, password);
                    System.out.println(message);
                    // 发送消息
                    ctx.writeAndFlush(message);
                    System.out.println("等待后续操作...");
                    try {
                        WAIT_FOR_LOGIN.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 如果登录失败
                    if (!LOGIN.get()) {
                        ctx.channel().close();
                        return;
                    }
                    while (true) {
                        System.out.println("==================================");
                        System.out.println("send [username] [content]");
                        System.out.println("gsend [group name] [content]");
                        System.out.println("gcreate [group name] [m1,m2,m3...]");
                        System.out.println("gmembers [group name]");
                        System.out.println("gjoin [group name]");
                        System.out.println("gquit [group name]");
                        System.out.println("quit");
                        System.out.println("==================================");
                        String command = null;
                        try {
                            command = scanner.nextLine();
                        } catch (Exception e) {
                            break;
                        }
                        if(EXIT.get()){
                            return;
                        }
                        String[] s = command.split(" ");
                        switch (s[0]){
                            case "quit":
                                ctx.channel().close();
                                return;
                        }
                    }
                }, "system in").start();
            }

            // 在连接断开时触发
            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                log.debug("连接已经断开，按任意键退出..");
                EXIT.set(true);
            }

            // 在出现异常时触发
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                log.debug("连接已经断开，按任意键退出..{}", cause.getMessage());
                EXIT.set(true);
            }
        });
    }
}
