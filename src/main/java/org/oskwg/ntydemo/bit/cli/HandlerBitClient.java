package org.oskwg.ntydemo.bit.cli;

import org.oskwg.ntydemo.bit.codec.entity.BitModBusRequest;
import org.oskwg.ntydemo.bit.codec.entity.BitModBusResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.osgl.util.S;

public class HandlerBitClient extends SimpleChannelInboundHandler<BitModBusResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BitModBusResponse response) throws Exception {
        StringBuffer sb = new StringBuffer();
        for (Byte value : response.getValues()) {
//            System.out.print(Byte.toUnsignedInt(value) + " ");
//            System.out.println(S.padLeadingZero(Integer.parseInt(), 8));
            sb.append(S.reversed(S.padLeadingZero(Integer.parseInt(Integer.toBinaryString(Byte.toUnsignedInt(value))), 8)));
        }
        System.out.println(sb);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 最大可发送FF个字节，也就是256*8=2048个字节
        ctx.write(new BitModBusRequest((short) 1, (short) 0, 1401));
//        ctx.write(new ModBusRequest((short) 2, (short) 126, 126));
//        ctx.write(new ModBusRequest((short) 3, (short) (126 * 2), 126));
//        ctx.write(new ModBusRequest((short) 4, (short) (126 * 3), 126));
//        ctx.write(new ModBusRequest((short) 5, (short) (126 * 4), 126));
//        ctx.write(new ModBusRequest((short) 6, (short) (126 * 5), 126));
//        ctx.write(new ModBusRequest((short) 7, (short) (126 * 6), 794 - (126 * 6) ));

        // 1111-1111 1111-1111 1111-1111 1010-1001
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
