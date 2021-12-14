package org.oskwg.ntydemo.nty.srv;

import org.oskwg.ntydemo.nty.codec.ModBusRequestDecoder;
import org.oskwg.ntydemo.nty.codec.ModBusResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;

public class NtyServer {

    public static final int PORT = 9002;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(10);
        try {
            // 配置服务器的NIO线程租
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 4, 2, 0, 0));
                            pipeline.addLast(new ModBusRequestDecoder());
                            pipeline.addLast(new ModBusResponseEncoder());
                            // 设置超时时间，防止连接过多。
                            pipeline.addLast("readTimeout", new ReadTimeoutHandler(5));
                            pipeline.addLast(businessGroup, "executor", new HandlerServer());
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind().sync();
            System.out.println("access address:" + f.channel().localAddress());
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }
}
