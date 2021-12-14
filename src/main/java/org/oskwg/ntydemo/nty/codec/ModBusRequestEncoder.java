package org.oskwg.ntydemo.nty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;

public class ModBusRequestEncoder extends MessageToByteEncoder<ModBusRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ModBusRequest msg, ByteBuf out) throws Exception {
        // 数据
        ByteBuf buf = ctx.alloc().buffer(6);
        try {
            buf.writeByte(0x01);
            buf.writeByte(0x03);
            buf.writeShort(msg.getStartAddress());
            buf.writeShort(msg.getCount());

            // 构建帧
            out.writeShort(msg.getSequenceId());
            out.writeShort(0x0000);
            out.writeShort(buf.readableBytes());
            out.writeBytes(buf);
        }finally {
            ReferenceCountUtil.release(buf);
        }
    }
}
