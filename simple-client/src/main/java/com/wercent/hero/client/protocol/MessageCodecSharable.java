package com.wercent.hero.client.protocol;

import com.wercent.hero.client.config.Config;
import com.wercent.hero.client.message.Message;
import com.wercent.hero.client.utils.TypeInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;


/**
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    @Value("${serializer.algorithm:Json}")
    private String serializerAlgorithm;

    private final String magic = "@he";

    private final TypeInfo typeInfo;

    public MessageCodecSharable(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 1. 3 字节的魔数
        out.writeBytes(magic.getBytes());
        // 2. 1 字节的序列化方式 json 0
        out.writeByte(Config.getSerializerAlgorithm(serializerAlgorithm).ordinal());
        // 类型
        byte[] messageType = typeInfo.getNameByType(msg.getClass()).getBytes();
        // 3. 4 字节消息类型长度
        out.writeInt(messageType.length);
        // 正文内容
        byte[] bytes = Config.getSerializerAlgorithm().serialize(msg);
        // --------- 消息体 ------------
        // 3. 剩余总长度: 正文  + 消息类型
        out.writeInt(bytes.length + messageType.length);
        // 5. 消息类型
        out.writeBytes(messageType);
        // 6. 正文长度
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("我执行了");
        byte[] magic = new byte[this.magic.length()];
        in.readBytes(magic, 0, this.magic.length());
        String receiveMagic = new String(magic);
        if (!this.magic.equals(receiveMagic)) {
//            ctx.writeAndFlush("无效的数据包");
            log.debug("无效的数据, 魔数对应失败: {}", receiveMagic);
            return;
        }
        // 序列化算法
        byte serializerAlgorithm = in.readByte(); // 0
        // 类型长度
        int typeLength = in.readInt();
        // 总长度
        int totalLength = in.readInt();
        // 类型内容
        byte[] byteType = new byte[typeLength];
        in.readBytes(byteType, 0, typeLength);
        String type = new String(byteType, Charset.defaultCharset());
        // 正文内容
        int contentLength = totalLength - typeLength;
        byte[] content = new byte[contentLength];
        in.readBytes(content, 0, contentLength);
        // 找到反序列化算法
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerAlgorithm];
        // 确定具体消息类型
        Class<?> messageClass;
        try {
            messageClass = typeInfo.getTypeByName(type);
        } catch (Exception e) {
            log.error("找不到对应的处理模块: {}", type, e);
//            ctx.writeAndFlush("找不到对应的处理模块: " + type);
            return;
        }
        Message message = (Message) algorithm.deserialize(messageClass, content);
        log.info("接收到的数据为类型为: {}", message.getClass());
        out.add(message);
    }

}
