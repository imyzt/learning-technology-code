package top.imyzt.learning.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static top.imyzt.learning.netty.Constants.SOCKET_PORT;

/**
 * @author imyzt
 * @date 2022/07/02
 * @description Netty Server
 */
@Component
@Slf4j
public class NettyServer {

    @Resource
    private ObjectMapper objectMapper;

    @PostConstruct
    @Order(HIGHEST_PRECEDENCE)
    public void start() throws InterruptedException {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        socketChannel.pipeline().addLast(new StringDecoder());
                        socketChannel.pipeline().addLast(new StringEncoder());
                        socketChannel.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("Send msg to client, msg={}", msg);
                                super.write(ctx, msg, promise);
                            }
                        });
                        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                if (msg instanceof String clientMsg) {
                                    var message = objectMapper.readValue(clientMsg, Message.class);

                                    log.info("server read message, messageId={}, content={}", message.messageId(), message.content());

                                    // 响应客户端消息
                                    ctx.writeAndFlush("server receive, to client: " + clientMsg);

                                    super.channelRead(ctx, msg);
                                }
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) {
                                log.info("Server Channel Active");
                            }
                        });
                    }
                });

        Channel channel = bootstrap.bind(SOCKET_PORT).sync().channel();

        channel.closeFuture().addListener((ChannelFutureListener) channelClose -> {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.info("server close");
        });

    }

}