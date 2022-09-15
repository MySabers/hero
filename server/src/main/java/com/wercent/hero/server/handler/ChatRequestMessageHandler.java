package com.wercent.hero.server.handler;


import com.wercent.hero.common.message.ChatRequestMessage;
import com.wercent.hero.common.message.ChatResponseMessage;
import com.wercent.hero.server.broadcast.UserBroadcast;
import com.wercent.hero.server.session.Session;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@ChannelHandler.Sharable
@Controller
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    private final Session session;

    private final UserBroadcast userBroadcast;

    public ChatRequestMessageHandler(UserBroadcast userBroadcast, Session session) {
        this.userBroadcast = userBroadcast;
        this.session = session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage chatRequestMessage) throws Exception {
        ChatResponseMessage chatResponseMessage =
                new ChatResponseMessage(session.getName(ctx.channel()), chatRequestMessage.getContent(), true, "");
        userBroadcast.chatBroadcast(chatResponseMessage);
    }

}
