package com.wercent.hero.protocol;

import com.wercent.hero.config.Config;
import com.wercent.hero.message.Message;
import com.wercent.hero.message.MessageWrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    private final String magic = "@hero-c";

    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 1. 5 字节的魔数
        out.writeBytes(magic.getBytes());
        // 2. 1 字节的序列化方式 json 0
        out.writeByte(Config.getSerializerAlgorithm().ordinal());
        // 3. 包装对象
        MessageWrap<Message> messageMessageWrap = new MessageWrap<>();
        byte[] bytes = Config.getSerializerAlgorithm().serialize(messageMessageWrap);
        // 7. 长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        String receiveMagic =
        byte[] magic = new byte[this.magic.length()];
        in.readBytes(magic, 0, this.magic.length());
        String receiveMagic = new String(magic);
        if (this.magic.equals(receiveMagic)) {
            ctx.writeAndFlush("无效的是数据包");
            log.debug("无效的数据, 魔数对应失败: {}", receiveMagic);
            return;
        }
        byte serializerAlgorithm = in.readByte(); // 0
        byte messageType = in.readByte(); // 0,1,2...
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        // 找到反序列化算法
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerAlgorithm];
        // 确定具体消息类型
        Class<? extends Message> messageClass = Message.getMessageClass(messageType);
        Message message = algorithm.deserialize(messageClass, bytes);
        out.add(message);
    }

}
