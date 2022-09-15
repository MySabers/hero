package com.wercent.hero.client.handler;

import com.wercent.hero.client.gui.Index;
import com.wercent.hero.common.message.ChatResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Controller;

@Controller
public class ChatResponseHandler extends SimpleChannelInboundHandler<ChatResponseMessage> {

    private final Index indexFrame;

    public ChatResponseHandler(Index indexFrame) {
        this.indexFrame = indexFrame;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatResponseMessage chatResponseMessage) throws Exception {
        System.out.println(chatResponseMessage);
        indexFrame.addMessage(chatResponseMessage);
    }

}
