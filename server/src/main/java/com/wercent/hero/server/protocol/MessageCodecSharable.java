package com.wercent.hero.server.protocol;


import com.wercent.hero.server.config.Config;
import com.wercent.hero.server.message.Message;
import com.wercent.hero.server.message.MessageWrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    private final String magic = "@hero-c";

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        System.out.println("我执行了吗");
        ByteBuf out = ctx.alloc().buffer();
        // 1. 7 字节的魔数
        out.writeBytes(magic.getBytes());
        // 2. 1 字节的序列化方式 json 0
        out.writeByte(Config.getSerializerAlgorithm(serializerAlgorithm).ordinal());
        // 包装对象
        String messageType = applicationContext.getBeanNamesForType(msg.getClass())[0];
        MessageWrap messageMessageWrap = new MessageWrap(1, messageType, msg);
        byte[] bytes = Config.getSerializerAlgorithm().serialize(messageMessageWrap);
        // 3. 长度
        out.writeInt(bytes.length);
        // 4. 写入内容
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] magic = new byte[this.magic.length()];
        in.readBytes(magic, 0, this.magic.length());
        String receiveMagic = new String(magic);
        if (this.magic.equals(receiveMagic)) {
            ctx.writeAndFlush("无效的是数据包");
            log.debug("无效的数据, 魔数对应失败: {}", receiveMagic);
            return;
        }
        byte serializerAlgorithm = in.readByte(); // 0
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        // 找到反序列化算法
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerAlgorithm];
        // 确定具体消息类型
        MessageWrap message = algorithm.deserialize(MessageWrap.class, bytes);
//        Class<?> messageClass = applicationContext.getType(message.getMessageType());
        log.info("接收到的数据为类型为: {}", message.getBody().getClass());
        out.add(message.getBody());
    }

}
