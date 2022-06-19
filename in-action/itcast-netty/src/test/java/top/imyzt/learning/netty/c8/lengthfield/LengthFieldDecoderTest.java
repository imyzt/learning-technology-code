package top.imyzt.learning.netty.c8.lengthfield;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 16:39:08.490 [main] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0xembedded, L:embedded - R:embedded] READ: 12B
 *          +-------------------------------------------------+
 *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 * +--------+-------------------------------------------------+----------------+
 * |00000000| 48 65 6c 6c 6f 20 57 6f 72 6c 64 21             |Hello World!    |
 * +--------+-------------------------------------------------+----------------+
 * 16:39:08.490 [main] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0xembedded, L:embedded - R:embedded] READ: 3B
 *          +-------------------------------------------------+
 *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 * +--------+-------------------------------------------------+----------------+
 * |00000000| 48 69 21                                        |Hi!             |
 * +--------+-------------------------------------------------+----------------+
 * 16:39:08.490 [main] DEBUG io.netty.handler.logging.LoggingHandler - [id: 0xembedded, L:embedded - R:embedded] READ COMPLETE
 *
 * @author imyzt
 * @date 2022/06/19
 * @description 协议头自定义, 长度计算出来
 */
@Slf4j
public class LengthFieldDecoderTest {

    public static void main(String[] args) {

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                //    1.  lengthFieldOffset - 长度字段偏移量
                //    2.  lengthFieldLength - 长度字段长度
                //    3.  lengthAdjustment - 长度字段为基准， 还有跳过几个字节才是内容（可以放协议版本之类的）
                //    4.  initialBytesToStrip - 从头剥离几个字节（可以去掉长度字段）
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        send(buf, "Hello World!");
        send(buf, "Hi!");
        embeddedChannel.writeInbound(buf);

    }

    private static void send(ByteBuf buf, String content) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        // 消息长度, 占用4byte(int)
        buf.writeInt(bytes.length);
        // 版本号, 占用1byte
        buf.writeByte(1);
        // 消息的内容
        buf.writeBytes(bytes);
    }
}