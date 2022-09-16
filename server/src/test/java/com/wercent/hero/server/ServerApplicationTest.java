//package com.wercent.hero.server;
//
//import com.wercent.hero.common.message.login.LoginRequestMessage;
//import com.wercent.hero.common.message.Message;
//import com.wercent.hero.common.protocol.MessageCodecSharable;
//import com.wercent.hero.common.utils.TypeInfo;
//import com.wercent.hero.server.config.Config;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufAllocator;
//import io.netty.channel.embedded.EmbeddedChannel;
//import io.netty.handler.logging.LoggingHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//
//import java.lang.annotation.Annotation;
//import java.util.List;
//
//@Slf4j
//public class ServerApplicationTest {
//
//
//    @Test
//    public void main() {
//
//        TypeInfo typeInfo = new TypeInfo() {
//            @Override
//            public String getNameByType(Class<?> type) {
//                return "loginRequest";
//            }
//
//            @Override
//            public Class<?> getTypeByName(String name) {
//                return LoginRequestMessage.class;
//            }
//
//            @Override
//            public List<Object> getBeansWithAnnotation(Class<? extends Annotation> anno) {
//                return null;
//            }
//        };
//
//        MessageCodecSharable CODEC = new MessageCodecSharable(typeInfo);
//        LoggingHandler LOGGING = new LoggingHandler();
//        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);
//
//        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
////        channel.writeOutbound(message);
//        ByteBuf buf = messageToByteBuf(message, typeInfo);
//        channel.writeInbound(buf);
//
//    }
//
//
//    public ByteBuf messageToByteBuf(Message msg, TypeInfo typeInfo) {
//        int algorithm = 0;
//        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
//        out.writeBytes("@he".getBytes());
//        out.writeByte(0);
//        // 类型
//        byte[] messageType = typeInfo.getNameByType(msg.getClass()).getBytes();
//        // 消息类型长度
//        out.writeInt(messageType.length);
//        byte[] bytes = Config.getSerializerAlgorithm().serialize(msg);
//        // --------- 消息体 ------------
//        // 3. 剩余总长度: 正文长度 + 消息类型长度 + 消息类型
//        out.writeInt(bytes.length + messageType.length);
//        // 5. 消息类型
//        out.writeBytes(messageType);
//        // 6. 正文长度
//        out.writeBytes(bytes);
//        return out;
//    }
//}
//
