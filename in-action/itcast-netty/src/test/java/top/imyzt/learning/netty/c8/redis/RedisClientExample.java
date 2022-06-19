package top.imyzt.learning.netty.c8.redis;

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

/**
 * @author imyzt
 * @date 2022/06/19
 * @description 使用redis协议, 访问redis服务端
 */
@Slf4j
public class RedisClientExample {

    public static void main(String[] args) {
        // 换行符
        byte[] LINE = {13, 10};
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
                                public void channelActive(ChannelHandlerContext ctx) {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    writeCommand(buf, "*3");
                                    writeCommand(buf, "$3");
                                    writeCommand(buf, "set");

                                    String key = "name";
                                    writeCommand(buf, "$" + key.getBytes(StandardCharsets.UTF_8).length);
                                    writeCommand(buf, key);

                                    String value = "abc";
                                    writeCommand(buf, "$" + value.getBytes(StandardCharsets.UTF_8).length);
                                    writeCommand(buf, value);

                                    // 写入通道
                                    ctx.writeAndFlush(buf);
                                }

                                private void writeCommand(ByteBuf buf, String command) {
                                    buf.writeBytes(command.getBytes());
                                    buf.writeBytes(LINE);
                                }
                            });
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            ByteBuf buf = (ByteBuf) msg;
                            System.out.println("redis resp: " + buf.toString(StandardCharsets.UTF_8));
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 6379).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server err", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}