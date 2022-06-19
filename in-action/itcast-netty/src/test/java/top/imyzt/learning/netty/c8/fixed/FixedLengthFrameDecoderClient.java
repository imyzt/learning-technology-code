package top.imyzt.learning.netty.c8.fixed;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author imyzt
 * @date 2022/06/19
 * @description 粘包/半包
 */
@Slf4j
public class FixedLengthFrameDecoderClient {

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
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                // 连接channel建立成功后, 触发active事件
                                public void channelActive(ChannelHandlerContext ctx) {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    Random random = new Random();
                                    char c = '0';
                                    for (int i = 0; i < 10; i++) {
                                        byte[] bytes = fill10Bytes(c, random.nextInt(10) + 1);
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


    private static byte[] fill10Bytes(char c, int len) {
        int maxLen = 10;
        byte[] bytes = new byte[maxLen];
        len = Math.min(len, maxLen);
        for (int i = 0; i < maxLen; i++) {
            if ((i < len)) {
                bytes[i] = (byte) c;
            } else {
                bytes[i] =  (byte) '_';
            }
        }
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
        return bytes;
    }
}