package org.oskwg.ntydemo.bit.codec;

import org.oskwg.ntydemo.bit.codec.entity.BitModBusRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

public class BitModBusRequestEncoder extends MessageToByteEncoder<BitModBusRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, BitModBusRequest msg, ByteBuf out) throws Exception {
        // 数据
        ByteBuf buf = ctx.alloc().buffer(6);
        try {
            buf.writeByte(0x01);
            buf.writeByte(0x02);
            buf.writeShort(msg.getStartAddress());
            buf.writeShort(msg.getCount());

            // 构建帧
            out.writeShort(msg.getSequenceId());
            out.writeShort(0x0000);
            out.writeShort(buf.readableBytes());
            out.writeBytes(buf);
        } finally {
            ReferenceCountUtil.release(buf);
        }
    }
}
