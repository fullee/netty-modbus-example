package org.oskwg.ntydemo.bit.cli;

import org.oskwg.ntydemo.bit.codec.BitModBusRequestEncoder;
import org.oskwg.ntydemo.bit.codec.BitModBusResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * 0x1F ---> 0001 1111 ---> 1byte
 * 0x1F2D ---> 0001 1111  0010 1101 ---> 2byte
 */
public class BitClient {

    public static final String HOST = "localhost";
    public static final int PORT = 2002;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 300; i++) {
            NioEventLoopGroup group = new NioEventLoopGroup();

            Bootstrap bs = new Bootstrap();
            try {
                bs.group(group)
                        .channel(NioSocketChannel.class)
                        .remoteAddress(new InetSocketAddress(HOST, PORT))
                        .handler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();
//                                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                                pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 4, 2, 0, 0));
                                pipeline.addLast(new BitModBusRequestEncoder());
                                pipeline.addLast(new BitModBusResponseDecoder());
                                pipeline.addLast(new HandlerBitClient());
                            }
                        });

                ChannelFuture future = bs.connect().sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully().sync();
            }
        }

    }
}
