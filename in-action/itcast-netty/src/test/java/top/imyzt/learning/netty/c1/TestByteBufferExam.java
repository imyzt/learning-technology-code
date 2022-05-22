package top.imyzt.learning.netty.c1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description 粘包 半包
 */
@Slf4j
public class TestByteBufferExam {

    public static void main(String[] args) {

        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,World\nI'm zhangsan\nHo".getBytes(StandardCharsets.UTF_8));
        split(source);
        source.put("w are you?\n".getBytes(StandardCharsets.UTF_8));
        split(source);

        //Hello,World
        //I'm zhangsan
        //How are you?
    }

    public static void split(ByteBuffer source) {

        source.flip();

        for (int i = 0; i < source.limit(); i++) {
            // 解决粘包问题,根据分隔符拆分
            if (source.get(i) == '\n') {
                // 本次读取的长度len
                int len = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(len);
                for (int j = 0; j < len; j++) {
                    target.put(source.get());
                }
                target.flip();
                System.out.println(StandardCharsets.UTF_8.decode(target).toString().replace("\n", ""));
            }
        }

        // 解决半包问题,未读取的内容留着下一次读
        source.compact();
    }
}