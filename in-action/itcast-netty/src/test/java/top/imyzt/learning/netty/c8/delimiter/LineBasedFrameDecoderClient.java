package top.imyzt.learning.netty.c8.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

/**
 * @author imyzt
 * @date 2022/06/19
 * @description 粘包/半包
 */
@Slf4j
public class LineBasedFrameDecoderClient {

    public static void main(String[] args) {

        send();
        log.info("finish");
    }

    private static void send() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                // 连接channel建立成功后, 触发active事件
                                public void channelActive(ChannelHandlerContext ctx) {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    Random random = new Random();
                                    char c = '0';
                                    for (int i = 0; i < 10; i++) {
                                        byte[] bytes = makeString(c, random.nextInt(256) + 1);
                                        c++;
                                        buf.writeBytes(bytes);
                                    }
                                    ctx.writeAndFlush(buf);
                                }
                            });
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 9999).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client err", e);
            Thread.currentThread().interrupt();
        } finally {
            worker.shutdownGracefully();
        }
    }

    private static byte[] makeString(char c, int len) {
        byte[] bytes = new byte[len + 1];
        Arrays.fill(bytes, (byte) c);
        bytes[len] = '\n';
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
        return bytes;
    }
}