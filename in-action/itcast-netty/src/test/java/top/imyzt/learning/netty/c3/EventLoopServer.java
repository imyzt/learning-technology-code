package top.imyzt.learning.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author imyzt
 * @date 2022/05/29
 * @description EventLoop服务端测试
 */
@Slf4j
public class EventLoopServer {

    public static void main(String[] args) {

        // 细分2: 创建一个独立的EventLoopGroup
        DefaultEventLoopGroup group = new DefaultEventLoopGroup(2);

        new ServerBootstrap()
                // 细分1: boss 只负责ServerSocketChannel上的accept事件, worker只负责SocketChannel上的读写事件
                .group(new NioEventLoopGroup(2), new NioEventLoopGroup(5))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast("handler-1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ByteBuf buf = (ByteBuf) msg;
                                log.info(buf.toString(StandardCharsets.UTF_8));

                                // 让消息传递给下一个handler
                                ctx.fireChannelRead(msg);
                            }
                            // 模拟耗时操作, 将IO操作交给另一个EventLoopGroup执行
                        }).addLast(group, "handler-2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ByteBuf buf = (ByteBuf) msg;
                                log.info(buf.toString(StandardCharsets.UTF_8));
                            }
                        });
                    }
                })
                .bind(9898);
    }
}