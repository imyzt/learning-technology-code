package top.imyzt.learning.netty;

import cn.hutool.core.thread.NamedThreadFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import static top.imyzt.learning.netty.Constants.SOCKET_PORT;

/**
 * @author imyzt
 * @date 2022/07/02
 * @description netty client
 */
@Slf4j
@Component
public class NettyClient {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 待发送消息队列
     */
    static final BlockingQueue<Message> SEND_QUEUE = new ArrayBlockingQueue<>(1024);

    private static final ExecutorService SOCKET_THREAD_POOL = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new NamedThreadFactory("message-channel", true),
            new CallerRunsPolicy()
    );

    @PostConstruct
    public void clientStart() {

        SOCKET_THREAD_POOL.execute(() -> {
            NioEventLoopGroup worker = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.info("receive message form Server -> {}", msg);
                                    super.channelRead(ctx, msg);
                                }
                            });
                        }
                    });

            try {
                Channel channel = bootstrap.connect("127.0.0.1", SOCKET_PORT).sync().channel();

                channel.closeFuture().addListener((ChannelFutureListener) channelFuture -> {
                    worker.shutdownGracefully();
                    log.info("client close");
                });

                while (true) {
                    // 阻塞等待消息
                    Message message = SEND_QUEUE.take();
                    channel.writeAndFlush(objectMapper.writeValueAsString(message));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

    }
}