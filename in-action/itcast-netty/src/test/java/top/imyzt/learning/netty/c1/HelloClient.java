package top.imyzt.learning.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author imyzt
 * @date 2022/05/29
 * @description netty simple client
 */
@Slf4j
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {

        // 1. 启动类
        new Bootstrap()
                // 2. 添加eventLoop
                .group(new NioEventLoopGroup())
                // 3. 客户端Channel实现
                .channel(NioSocketChannel.class)
                // 4. 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    // 连接建立后调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 5. 连接到服务器
                .connect("localhost", 9988)
                .sync()
                .channel()
                // 6. 向服务器发送数据
                .writeAndFlush("hello world");
    }
}