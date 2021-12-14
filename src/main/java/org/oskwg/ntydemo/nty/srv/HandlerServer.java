package org.oskwg.ntydemo.nty.srv;

import cn.hutool.core.util.RandomUtil;
import org.oskwg.ntydemo.nty.codec.ModBusRequest;
import org.oskwg.ntydemo.nty.codec.ModBusResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class HandlerServer extends SimpleChannelInboundHandler<ModBusRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ModBusRequest request) throws Exception {

        List<Float> values = new ArrayList<>(request.getCount() / 2);
        for (int i = 0; i < request.getCount() / 2; i++) {
            values.add(new Double(RandomUtil.randomDouble(100,2, RoundingMode.DOWN)).floatValue());
        }

        ModBusResponse response = new ModBusResponse(request.getSequenceId(), values);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}