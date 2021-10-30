package com.dianli2.nty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ModBusRequestDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        ModBusRequest request = new ModBusRequest();
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
