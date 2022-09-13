package com.wercent.hero.server;

import com.wercent.hero.server.config.Config;
import com.wercent.hero.server.message.LoginRequestMessage;
import com.wercent.hero.server.message.Message;
import com.wercent.hero.server.protocol.MessageCodecSharable;
import com.wercent.hero.server.protocol.Serializer;
import com.wercent.hero.server.utils.TypeInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ServerApplicationTest {


    @Test
    public void main() {

        TypeInfo typeInfo = new TypeInfo() {
            @Override
            public String getNameByType(Class<?> type) {
                return "loginRequesat";
            }

            @Override
            public Class<?> getTypeByName(String name) {
                return LoginRequestMessage.class;
            }
        };

        MessageCodecSharable CODEC = new MessageCodecSharable(typeInfo);
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);

        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
//        channel.writeOutbound(message);
        ByteBuf buf = messageToByteBuf(message, typeInfo);
        channel.writeInbound(buf);

    }


    public static ByteBuf messageToByteBuf(Message msg, TypeInfo typeInfo) {
        int algorithm = 0;
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes("@hero-c".getBytes());
        out.writeByte(0);
        // 类型
        byte[] messageType = typeInfo.getNameByType(msg.getClass()).getBytes();
        byte[] bytes = Config.getSerializerAlgorithm().serialize(msg);
        // --------- 消息体 ------------
        // 3. 剩余总长度: 正文长度 + 消息类型长度 + 消息类型
        out.writeInt(bytes.length + 4 + messageType.length);
        // 4. 消息类型长度
        out.writeInt(messageType.length);
        // 5. 消息类型
        out.writeBytes(messageType);
        // 6. 正文长度
        out.writeBytes(bytes);
        return out;
    }
}

