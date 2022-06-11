package top.imyzt.learning.netty.c5;

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
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 出入站处理器
 */
@Slf4j
public class NettyPipelineTest {

    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        // head -> h1 -> h2 -> h3 -> h6 -> h5 -> h4 -> tail
                        pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("1");
                                ByteBuf byteBuf = (ByteBuf) msg;
                                String name = byteBuf.toString(StandardCharsets.UTF_8);
                                // byteBuf -> String
                                super.channelRead(ctx, name);
                            }
                        });
                        pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object name) throws Exception {
                                log.info("2");
                                Student student = new Student((String) name);
                                // String -> Student
                                super.channelRead(ctx, student);
                            }
                        });
                        pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object student) {
                                log.info("3, student={}, class={}", student, student.getClass());
                                // 最后一个入站处理器, 无需再向后传递数据
                                // super.channelRead(ctx, msg);

                                // 向客户端写数据, 触发出站处理器
                                // 从tail(最后) 往前找出站处理器
                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes(StandardCharsets.UTF_8)));
                                // 从[当前]处理器向前找出站处理器
                                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes(StandardCharsets.UTF_8)));

                            }
                        });
                        pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("4");
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("5");
                                super.write(ctx, msg, promise);
                            }
                        });
                        pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("6");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(9898);
    }

    @ToString
    @AllArgsConstructor
    public static class Student {
        private final String name;
    }

}