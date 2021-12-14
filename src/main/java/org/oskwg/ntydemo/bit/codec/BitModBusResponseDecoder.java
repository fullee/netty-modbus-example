package org.oskwg.ntydemo.bit.codec;

import cn.hutool.core.util.ByteUtil;
import org.oskwg.ntydemo.bit.codec.entity.BitModBusResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.ArrayList;
import java.util.List;

public class BitModBusResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 判断 【当前包的数据长度（setValueByteLength） + 包头长度】 是否可以读取
        if (in.readableBytes() >= ByteUtil.byteToUnsignedInt(in.getByte(8)) + 9) {
            BitModBusResponse response = new BitModBusResponse();
            response.setSequenceId(in.readShort());
            response.setIdentifier(in.readShort());
            response.setProtocolLength(in.readShort());
            response.setUnitIdentifier(in.readByte());
            response.setFunCode(in.readByte());
            response.setValueByteLength(ByteUtil.byteToUnsignedInt(in.readByte()));
            // 可读字节数大于等于要收的字节数
            if (in.readableBytes() >= response.getValueByteLength()) {
                List<Byte> values = new ArrayList<>();
                for (int i = 0; i < response.getValueByteLength(); i++) {
                    values.add(in.readByte());
                }
                response.setValues(values);
                out.add(response);
            }
        }
    }
}
