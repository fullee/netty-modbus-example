package org.oskwg.ntydemo.bit.codec;

import org.oskwg.ntydemo.bit.codec.entity.BitModBusResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

public class BitModBusResponseEncoder extends MessageToByteEncoder<BitModBusResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, BitModBusResponse msg, ByteBuf out) throws Exception {
        // 构造值
        ByteBuf protocol = ctx.alloc().buffer();
        try {
            // 构造协议
            protocol.writeByte(0x01);
            protocol.writeByte(0x02);
            protocol.writeByte(msg.getValues().size());
            msg.getValues().forEach(protocol::writeByte);

            // 构造帧
            out.writeShort(msg.getSequenceId());
            out.writeShort(0x0000);
            out.writeShort(protocol.readableBytes());
            out.writeBytes(protocol);
        } finally {
            ReferenceCountUtil.release(protocol);
        }
    }
}
