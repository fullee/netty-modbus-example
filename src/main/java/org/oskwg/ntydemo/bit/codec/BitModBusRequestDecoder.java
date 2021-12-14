package org.oskwg.ntydemo.bit.codec;

import org.oskwg.ntydemo.bit.codec.entity.BitModBusRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class BitModBusRequestDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 12) {
            BitModBusRequest request = new BitModBusRequest();
            request.setSequenceId(in.readShort());
            request.setIdentifier(in.readShort());
            request.setProtocolLength(in.readShort());
            request.setUnitIdentifier(in.readByte());
            request.setFunCode(in.readByte());
            request.setStartAddress(in.readShort());
            request.setCount(in.readShort());
            out.add(request);
        }
    }
}
