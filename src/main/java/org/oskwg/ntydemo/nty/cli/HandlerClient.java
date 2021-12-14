package org.oskwg.ntydemo.nty.cli;

import com.alibaba.fastjson.JSON;
import org.oskwg.ntydemo.nty.codec.ModBusRequest;
import org.oskwg.ntydemo.nty.codec.ModBusResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HandlerClient extends SimpleChannelInboundHandler<ModBusResponse> {

    List<Float> values = new ArrayList<>(397);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ModBusResponse response) throws Exception {

        values.addAll(response.getValues());
        if (values.size() == 397) {
            System.out.println(JSON.toJSONString(values.stream().sorted().collect(Collectors.toList())));
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ctx.write(new ModBusRequest((short) 1, (short) 0, 126));
        ctx.write(new ModBusRequest((short) 2, (short) 126, 126));
        ctx.write(new ModBusRequest((short) 3, (short) (126 * 2), 126));
        ctx.write(new ModBusRequest((short) 4, (short) (126 * 3), 126));
        ctx.write(new ModBusRequest((short) 5, (short) (126 * 4), 126));
        ctx.write(new ModBusRequest((short) 6, (short) (126 * 5), 126));
        ctx.write(new ModBusRequest((short) 7, (short) (126 * 6), 794 - (126 * 6) ));
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
