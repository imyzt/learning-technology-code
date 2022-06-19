package top.imyzt.learning.netty.c8;

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

/**
 *
 * 粘包: 消息被合并,客户端发送了10次消息,合并到3次就接收完了
 * 11:51:33.240 [nioEventLoopGroup-3-5] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0x4b0a634e, L:/127.0.0.1:9999 - R:/127.0.0.1:62262] REGISTERED
 * 11:51:33.240 [nioEventLoopGroup-3-5] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0x4b0a634e, L:/127.0.0.1:9999 - R:/127.0.0.1:62262] ACTIVE
 * 11:51:33.295 [nioEventLoopGroup-3-5] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0x4b0a634e, L:/127.0.0.1:9999 - R:/127.0.0.1:62262] READ: 96B
 *          +-------------------------------------------------+
 *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 * +--------+-------------------------------------------------+----------------+
 * |00000000| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * |00000010| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * |00000020| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * |00000030| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * |00000040| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * |00000050| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * +--------+-------------------------------------------------+----------------+
 * 11:51:33.295 [nioEventLoopGroup-3-5] DEBUG io.netty.channel.DefaultChannelPipeline - Discarded inbound message PooledUnsafeDirectByteBuf(ridx: 0, widx: 96, cap: 2048) that reached at the tail of the pipeline. Please check your pipeline configuration.
 * 11:51:33.296 [nioEventLoopGroup-3-5] DEBUG io.netty.channel.DefaultChannelPipeline - Discarded message pipeline : [LoggingHandler#0, DefaultChannelPipeline$TailContext#0]. Channel : [id: 0x4b0a634e, L:/127.0.0.1:9999 - R:/127.0.0.1:62262].
 * 11:51:33.296 [nioEventLoopGroup-3-5] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0x4b0a634e, L:/127.0.0.1:9999 - R:/127.0.0.1:62262] READ COMPLETE
 * 11:51:33.296 [nioEventLoopGroup-3-5] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0x4b0a634e, L:/127.0.0.1:9999 - R:/127.0.0.1:62262] READ: 64B
 *          +-------------------------------------------------+
 *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 * +--------+-------------------------------------------------+----------------+
 * |00000000| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * |00000010| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * |00000020| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * |00000030| 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f |................|
 * +--------+-------------------------------------------------+----------------+
 * 11:51:33.299 [nioEventLoopGroup-3-5] DEBUG io.netty.channel.DefaultChannelPipeline - Discarded inbound message PooledUnsafeDirectByteBuf(ridx: 0, widx: 64, cap: 2048) that reached at the tail of the pipeline. Please check your pipeline configuration.
 * 11:51:33.300 [nioEventLoopGroup-3-5] DEBUG io.netty.channel.DefaultChannelPipeline - Discarded message pipeline : [LoggingHandler#0, DefaultChannelPipeline$TailContext#0]. Channel : [id: 0x4b0a634e, L:/127.0.0.1:9999 - R:/127.0.0.1:62262].
 * 11:51:33.300 [nioEventLoopGroup-3-5] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0x4b0a634e, L:/127.0.0.1:9999 - R:/127.0.0.1:62262] READ COMPLETE
 *
 *
 * @author imyzt
 * @date 2022/06/19
 * @description 粘包/半包
 */
@Slf4j
public class HelloWorldClient {

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
                                    for (int i = 0; i < 10; i++) {
                                        ByteBuf buf = ctx.alloc().buffer(16);
                                        byte[] bytes = this.createFixedLenBytes(18);
                                        buf.writeBytes(bytes);
                                        // 写入消息
                                        ctx.writeAndFlush(buf);
                                        // 发完就关闭通道
                                        ctx.channel().close();
                                    }
                                }

                                private byte[] createFixedLenBytes(final int len) {
                                    byte[] bytes = new byte[len];
                                    for (int i = 0; i < bytes.length; i++) {
                                        bytes[i] = (byte) i;
                                    }
                                    return bytes;
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
}