package top.imyzt.learning.netty.c6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import static top.imyzt.learning.netty.c6.ByteBufTest.log;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 描述信息
 */
@Slf4j
public class ByteBufSliceTest {

    public static void main(String[] args) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'});
        log(buf);

        // 切片过程中,没有发生数据复制
        // 从0开始,拷贝5个
        ByteBuf f1 = buf.slice(0, 5);
        ByteBuf f2 = buf.slice(5, 5);
        f1.retain();
        log(f1);
        f2.retain();
        log(f2);

        // 会报错, 无法写入, 因为切片后的最大容量有限制
        // f1.writeByte('x');

        f1.setByte(0, 'z');
        log(f1);
        f1.release();
        log(buf);


        // 会报错, 因为原始内存已被释放, f1/f2都会被释放
        // 可以自己retain/release, 防止被别人release
        System.out.println("释放原有ByteBuf内存");
        f2.release();
        log(f2);

        //read index: 0 write index: 7 capacity: 10
        //         +-------------------------------------------------+
        //         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
        //+--------+-------------------------------------------------+----------------+
        //|00000000| 61 62 63 64 65 66 67                            |abcdefg         |
        //+--------+-------------------------------------------------+----------------+
        //read index: 0 write index: 5 capacity: 5
        //         +-------------------------------------------------+
        //         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
        //+--------+-------------------------------------------------+----------------+
        //|00000000| 61 62 63 64 65                                  |abcde           |
        //+--------+-------------------------------------------------+----------------+
        //read index: 0 write index: 5 capacity: 5
        //         +-------------------------------------------------+
        //         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
        //+--------+-------------------------------------------------+----------------+
        //|00000000| 66 67 00 00 00                                  |fg...           |
        //+--------+-------------------------------------------------+----------------+
        //read index: 0 write index: 5 capacity: 5
        //         +-------------------------------------------------+
        //         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
        //+--------+-------------------------------------------------+----------------+
        //|00000000| 7a 62 63 64 65                                  |zbcde           |
        //+--------+-------------------------------------------------+----------------+
        //read index: 0 write index: 7 capacity: 10
        //         +-------------------------------------------------+
        //         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
        //+--------+-------------------------------------------------+----------------+
        //|00000000| 7a 62 63 64 65 66 67                            |zbcdefg         |
        //+--------+-------------------------------------------------+----------------+

    }
}