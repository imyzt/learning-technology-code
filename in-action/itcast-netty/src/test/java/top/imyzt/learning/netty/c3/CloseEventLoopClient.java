package top.imyzt.learning.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 控制台输入结果发送服务端,优雅关闭
 */
@Slf4j
public class CloseEventLoopClient {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();

        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG))
                                .addLast(new StringEncoder());
                    }
                }).connect("localhost", 9898)
                .sync()
                .channel();

        log.info("channel={}", channel);

        // 循环等待本地输入
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if ("q".equals(line)) {
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();


        // 同步等待的关闭处理方法
        /*log.info("waiting close...");
        channel.closeFuture().sync();
        log.info("Sync CloseHandler, {}", channel);*/

        // 注册一个异步关闭处理方法
        channel.closeFuture().addListener((ChannelFutureListener) channelFuture -> {
            log.info("CloseHandler, {}", channelFuture);
            group.shutdownGracefully();
        });
    }
}