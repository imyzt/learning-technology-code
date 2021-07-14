package top.imyzt.nio;

import java.nio.IntBuffer;

/**
 * @author imyzt
 * @date 2021/05/09
 * @description NIO Buffer学习
 */
public class BasicNIOBuffer {

    public static void main(String[] args) {

        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < intBuffer.capacity(); i++) {
            int num = i * 4;
            intBuffer.put(num);
            System.out.println("写入Buffer: " + num);
        }

        // 读写切换
        intBuffer.flip();
        System.out.println();

        while (intBuffer.hasRemaining()) {
            System.out.println("读取Buffer: " + intBuffer.get());
        }

    }
}