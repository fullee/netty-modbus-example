package org.oskwg.ntydemo.nty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

public class ModBusResponseEncoder extends MessageToByteEncoder<ModBusResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ModBusResponse msg, ByteBuf out) throws Exception {
        // 构造值
        ByteBuf valuesBuf = ctx.alloc().buffer();
        ByteBuf protocol = ctx.alloc().buffer();
        try {
            msg.getValues().forEach(valuesBuf::writeFloat);
            if (valuesBuf.readableBytes() > 252) {
                throw new RuntimeException("valueByteLength can not >252,63 float number is 252 bytes");
            }
            // 构造协议
            protocol.writeByte(0x01);
            protocol.writeByte(0x03);
            protocol.writeByte(valuesBuf.readableBytes());
            protocol.writeBytes(valuesBuf);
            // 构造帧
            out.writeShort(msg.getSequenceId());
            out.writeShort(0x0000);
            out.writeShort(protocol.readableBytes());
            out.writeBytes(protocol);
        } finally {
            ReferenceCountUtil.release(valuesBuf);
            ReferenceCountUtil.release(protocol);
        }
    }
}
