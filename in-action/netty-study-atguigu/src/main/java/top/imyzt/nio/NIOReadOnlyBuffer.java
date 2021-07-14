package top.imyzt.nio;

import java.nio.ByteBuffer;

/**
 * @author imyzt
 * @date 2021/06/06
 * @description 只读缓冲区
 */
public class NIOReadOnlyBuffer {

    public static void main(String[] args) {


        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        byteBuffer.flip();

        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        // 不能写入 -> ReadOnlyBufferException
        readOnlyBuffer.put((byte) 111);
    }
}