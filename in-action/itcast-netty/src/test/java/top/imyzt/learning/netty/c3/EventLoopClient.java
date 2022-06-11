package top.imyzt.learning.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author imyzt
 * @date 2022/05/29
 * @description EventLoopClient
 */
@Slf4j
public class EventLoopClient {

    public static void main(String[] args) throws InterruptedException {

        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 1. 连接到服务器
                // 异步非阻塞, main发起了调用, 真正执行connect的是NioEventLoop线程
                .connect("localhost", 9898);

        // 2.1 使用sync同步处理结果
        // 阻塞住当前线程,直到NIO线程连接建立完毕
        /*channelFuture.sync();
        Channel channel = channelFuture.channel();
        log.info("channel={}", channel);
        channel.writeAndFlush("hello world");*/

        // 2.2 使用addListener(回调对象)方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // 在NioEventLoop线程连接建立好之后, 会调用operationComplete
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                channel.writeAndFlush("hhh");
            }
        });

    }
}