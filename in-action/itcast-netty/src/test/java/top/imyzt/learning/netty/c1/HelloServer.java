package top.imyzt.learning.netty.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author imyzt
 * @date 2022/05/29
 * @description Netty 普通服务端
 */
@Slf4j
public class HelloServer {

    public static void main(String[] args) {

        // 1. 启动器, 负责组装netty组件, 启动服务器
        new ServerBootstrap()
                // 2. BossEventLoop, WorkerEventLoop(selector, thread), group组
                .group(new NioEventLoopGroup())
                // 3. 选择 服务器的serverSocketChannel实现
                .channel(NioServerSocketChannel.class)
                // 4. boss负责处理连接, worker(child)负责处理读写, 决定了worker(child)能执行哪些操作(handler)
                .childHandler(
                        // 5. channel 代表和客户端进行数据读写的通道 Initializer负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 6. 添加具体handler
                        // 将ByteBuf转为字符串
                        ch.pipeline().addLast(new StringDecoder());
                        // 自定义handler
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                // 打印上方解码的数据
                                log.info("read...{}", msg);
                            }
                        });
                    }
                })
                // 7. 绑定监听端口
                .bind(9988);
    }
}