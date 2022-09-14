package com.wercent.hero.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class Client implements ApplicationRunner {
    @Resource
    private SocketInitializer socketInitializer;

    @Getter
    private Bootstrap bootstrap;

    /**
     * netty 服务端口
     */
    @Value("${server.port:8088}")
    private int port;

    /**
     * netty 服务地址
     */
    @Value("${server.host:localhost}")
    private String host;

    /**
     * 初始化netty配置
     */
    private void init() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            this.bootstrap = new Bootstrap();
            this.bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(this.socketInitializer);
            Channel channel = bootstrap.connect(this.host, this.port).sync().channel();
            log.info("Client connected address: {} and port: {} ", this.host, this.port);
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("Client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        this.init();
    }
}
