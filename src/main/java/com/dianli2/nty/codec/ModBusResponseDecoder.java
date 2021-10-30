package com.dianli2.nty.codec;

import cn.hutool.core.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.ArrayList;
import java.util.List;

public class ModBusResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ModBusResponse response = new ModBusResponse();
        response.setSequenceId(in.readShort());
        response.setIdentifier(in.readShort());
        response.setProtocolLength(in.readShort());
        response.setUnitIdentifier(in.readByte());
        response.setFunCode(in.readByte());
        response.setValueByteLength(ByteUtil.byteToUnsignedInt(in.readByte()));
        if (response.getValueByteLength() > 0) {
            List<Float> values = new ArrayList<>();
            for (int i = 0; i < response.getValueByteLength() / 4; i++) {
                values.add(in.readFloat());
            }
            response.setValues(values);
        }

        out.add(response);
    }
}
