package top.imyzt.learning.netty.c6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import lombok.extern.slf4j.Slf4j;

import static top.imyzt.learning.netty.c6.ByteBufTest.log;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 描述信息
 */
@Slf4j
public class CompositeByteBufTest {

    public static void main(String[] args) {

        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});

        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes(new byte[]{11, 21, 31, 41, 51});

        // 发生了两次数据拷贝
        /*ByteBuf all = ByteBufAllocator.DEFAULT.buffer();
        all.writeBytes(buf1).writeBytes(buf2);
        log(all);*/

        // 避免了数据的复制
        CompositeByteBuf compositeBuffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        compositeBuffer.addComponents(true, buf1, buf2);
        log(compositeBuffer);
    }
}