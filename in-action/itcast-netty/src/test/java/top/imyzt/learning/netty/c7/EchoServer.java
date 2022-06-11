package top.imyzt.learning.netty.c7;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 描述信息
 */
@Slf4j
public class EchoServer {

    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup(2), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder()).addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                String clientMsg = (String) msg;

                                String toClientMsg = clientMsg + "(from server)";

                                log.info("receive from client, msg={}, echo={}", clientMsg, toClientMsg);
                                // echo
                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes(toClientMsg.getBytes(StandardCharsets.UTF_8)));
                                super.channelRead(ctx, msg);
                            }
                        });
                        pipeline.addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("send Msg to client, msg={}", ((ByteBuf) msg).toString(StandardCharsets.UTF_8));
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(9999);
    }
}