package top.imyzt.learning.netty.chapter01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author imyzt
 * @date 2022/04/17
 * @description 引导客户端
 */
public class EchoClient {

    public static void main(String[] args) throws InterruptedException {

        String host = "127.0.0.1";
        int port = 8890;

        start(host, port);
    }

    private static void start(String host, int port) throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();

            bootstrap
                    // 指定EventLoopGroup以处理客户端事件, 需要适用于NIO的实现
                    .group(group)
                    // 适合NIO传输的Channel类型
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 连接到远程节点, 阻塞等待直到连接完成
            ChannelFuture channelFuture = bootstrap.connect().sync();
            // 阻塞, 直到Channel关闭
            channelFuture.channel().closeFuture().sync();

        } finally {
            // 关闭线程池, 并释放所有的资源
            group.shutdownGracefully().sync();
        }

    }
}