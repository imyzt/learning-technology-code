package top.imyzt.learning.netty.c1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description ByteBuffer与String互转
 */
@Slf4j
public class TestByteBufferString {

    public static void main(String[] args) {

        // 字符串 -> ByteBuffer

        // 1. 直接获取字节, 这种方式, 默认还在写模式
        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        buffer1.put("hello".getBytes(StandardCharsets.UTF_8));

        // 2. 通过charset, 直接可操作读模式
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");

        // 3. 通过wrap, 直接可操作读模式
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes(StandardCharsets.UTF_8));


        // ByteBuffer -> 字符串

        // buffer1 需要转读模式
        buffer1.flip();
        System.out.println(StandardCharsets.UTF_8.decode(buffer1).toString());
        // buffer2/3 不用转换
        System.out.println(StandardCharsets.UTF_8.decode(buffer2).toString());
        System.out.println(StandardCharsets.UTF_8.decode(buffer3).toString());

    }
}