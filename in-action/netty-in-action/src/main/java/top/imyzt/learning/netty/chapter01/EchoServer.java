package top.imyzt.learning.netty.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 1. 绑定到服务器将在其上监听并接受传入连接请求的端口
 * 2. 配置Channel, 已将有关的入站消息通知给EchoServerHandler实例
 *
 * @author imyzt
 * @date 2022/04/17
 * @description 引导服务器
 */
public class EchoServer {

    public static void main(String[] args) throws InterruptedException {

        int port = 8890;

        start(port);
    }

    private static void start(int port) throws InterruptedException {

        // 创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建ServerBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    // 指定所使用的的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加一个EchoServerHandle到子channel的channelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // EchoServerHannler被标注为@Shareable, 所以我们可以总是使用同样的实例
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            // 异步的绑定服务器, 调用sync()方法阻塞等待直到绑定完成
            ChannelFuture channelFuture = serverBootstrap.bind().sync();

            // 获取channel的CloseFuture, 并且阻塞当前线程直到它完成
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup释放所有资源
            group.shutdownGracefully().sync();
        }

    }
}