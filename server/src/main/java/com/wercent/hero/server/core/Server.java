package com.wercent.hero.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component()
public class Server implements ApplicationRunner {
    @Resource
    private SocketInitializer socketInitializer;

    @Getter
    private ServerBootstrap serverBootstrap;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * netty 服务监听端口
     */
    @Value("${netty.port:8088}")
    private int port;

    /**
     * 初始化netty配置
     */
    private void init() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            this.serverBootstrap = new ServerBootstrap();
            this.serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.socketInitializer);
            Channel channel = serverBootstrap.bind(this.port).sync().channel();
            log.info("Netty started on port: {} ", this.port);
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("Server error", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        this.init();
    }
}
