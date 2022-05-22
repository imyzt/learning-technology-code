package top.imyzt.learning.netty.c1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description 读模式自由切换
 */
@Slf4j
public class TestByteBufferRead {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();


        byte[] read = new byte[4];
        System.out.println(buffer.hasRemaining());
        // 读取到byte数组中
        buffer.get(read);
        System.out.println(buffer.hasRemaining());
        // reward 从头开始读
        buffer.rewind();
        System.out.println(buffer.hasRemaining());
        //true
        //false
        //true


        System.out.println();
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        // 加标记, 记录position的位置, 索引2的位置
        buffer.mark();
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        // 将position重置到索引2的位置
        buffer.reset();
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        //a
        //b
        //c
        //d
        //c
        //d

        System.out.println();
        System.out.println(buffer.position());
        // get(i) 不会改变position的位置
        System.out.println((char) buffer.get(0));
        System.out.println(buffer.position());
        //4
        //a
        //4
    }
}