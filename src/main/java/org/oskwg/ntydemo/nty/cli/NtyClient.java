package org.oskwg.ntydemo.nty.cli;

import org.oskwg.ntydemo.nty.codec.ModBusRequestEncoder;
import org.oskwg.ntydemo.nty.codec.ModBusResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * 0x1F ---> 0001 1111 ---> 1byte
 * 0x1F2D ---> 0001 1111  0010 1101 ---> 2byte
 */
public class NtyClient {

    public static final String HOST = "localhost";
    public static final int PORT = 9002;

    public static void main(String[] args) throws InterruptedException {
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
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 4, 2, 0, 0));
                            pipeline.addLast(new ModBusRequestEncoder());
                            pipeline.addLast(new ModBusResponseDecoder());
                            pipeline.addLast(new HandlerClient());
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
