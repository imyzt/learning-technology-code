package top.imyzt.learning.netty.c6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description ByteBuf 写入测试
 */
@Slf4j
public class ByteBufWriteTest {

    public static void main(String[] args) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);

        buf.writeBytes(new byte[]{1, 2, 3, 4});
        ByteBufTest.log(buf);

        // 默认先写高位,再写低位, 所以是00 00 00 05
        buf.writeInt(5);
        ByteBufTest.log(buf);

        //read index: 0 write index: 4 capacity: 256
        //         +-------------------------------------------------+
        //         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
        //+--------+-------------------------------------------------+----------------+
        //|00000000| 01 02 03 04                                     |....            |
        //+--------+-------------------------------------------------+----------------+
        //read index: 0 write index: 8 capacity: 256
        //         +-------------------------------------------------+
        //         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
        //+--------+-------------------------------------------------+----------------+
        //|00000000| 01 02 03 04 00 00 00 05                         |........        |
        //+--------+-------------------------------------------------+----------------+

        buf.writeInt(3);
        ByteBufTest.log(buf);
    }
}