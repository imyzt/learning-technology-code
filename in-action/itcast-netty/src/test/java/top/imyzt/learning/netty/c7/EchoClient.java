package top.imyzt.learning.netty.c7;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 描述信息
 */
@Slf4j
public class EchoClient {

    public static void main(String[] args) throws InterruptedException {

        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("write to server -> {}", ((ByteBuf) msg).toString(StandardCharsets.UTF_8));
                                super.write(ctx, msg, promise);
                            }
                        }).addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder()).addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("receive from server -> {}", msg);
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                })
                .connect("localhost", 9999);

        Channel channel = channelFuture.sync().channel();

        new Thread(() -> {

            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                channel.writeAndFlush(line);
            }
        }, "input").start();

    }
}