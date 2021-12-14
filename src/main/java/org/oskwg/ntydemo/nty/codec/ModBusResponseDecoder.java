package org.oskwg.ntydemo.nty.codec;

import cn.hutool.core.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.ArrayList;
import java.util.List;

public class ModBusResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 判断 【当前包的数据长度（setValueByteLength） + 包头长度】 是否可以读取
        if (in.readableBytes() >= ByteUtil.byteToUnsignedInt(in.getByte(8)) + 9) {
            ModBusResponse response = new ModBusResponse();
            response.setSequenceId(in.readShort());
            response.setIdentifier(in.readShort());
            response.setProtocolLength(in.readShort());
            response.setUnitIdentifier(in.readByte());
            response.setFunCode(in.readByte());
            response.setValueByteLength(ByteUtil.byteToUnsignedInt(in.readByte()));
            if (in.readableBytes() >= response.getValueByteLength()) {
                List<Float> values = new ArrayList<>();
                for (int i = 0; i < response.getValueByteLength() / 4; i++) {
                    values.add(in.readFloat());
                }
                response.setValues(values);
                out.add(response);
            }
        }
    }
}
