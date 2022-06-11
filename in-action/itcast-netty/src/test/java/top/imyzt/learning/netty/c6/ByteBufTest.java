package top.imyzt.learning.netty.c6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description Netty ByteBuf Created
 */
@Slf4j
public class ByteBufTest {

    public static void main(String[] args) {

        ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer();
        log(buffer);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append("a");
        }
        buffer.writeBytes(sb.toString().getBytes());
        log(buffer);
        System.out.println(buffer);

        //read index: 0 write index: 0 capacity: 256
        //
        //read index: 0 write index: 32 capacity: 256
        //         +-------------------------------------------------+
        //         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
        //+--------+-------------------------------------------------+----------------+
        //|00000000| 61 61 61 61 61 61 61 61 61 61 61 61 61 61 61 61 |aaaaaaaaaaaaaaaa|
        //|00000010| 61 61 61 61 61 61 61 61 61 61 61 61 61 61 61 61 |aaaaaaaaaaaaaaaa|
        //+--------+-------------------------------------------------+----------------+
    }

    public static void log(ByteBuf buf) {
        int len = buf.readableBytes();
        int rows = len / 16 + (len % 15 == 0 ? 0 : 1) + 4;
        StringBuilder builder = new StringBuilder(rows * 80 * 2)
                .append("read index: ").append(buf.readerIndex())
                .append(" write index: ").append(buf.writerIndex())
                .append(" capacity: ").append(buf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(builder, buf);
        System.out.println(builder.toString());
    }
}