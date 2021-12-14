package org.oskwg.ntydemo.bit.srv;

import cn.hutool.core.util.NumberUtil;
import org.oskwg.ntydemo.bit.codec.entity.BitModBusRequest;
import org.oskwg.ntydemo.bit.codec.entity.BitModBusResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class HandlerBitServer extends SimpleChannelInboundHandler<BitModBusRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BitModBusRequest request) throws Exception {

        int byteCount = (int) NumberUtil.div(request.getCount(), 8, 0, RoundingMode.UP);
        List<Byte> values = new ArrayList<>(byteCount);

        for (int i = 0; i < byteCount; i++) {
            if (i % 2 == 0) {
                values.add((byte) 1);
            }else {
                values.add((byte) 'a');
            }
        }

        BitModBusResponse response = new BitModBusResponse(request.getSequenceId(), values);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}