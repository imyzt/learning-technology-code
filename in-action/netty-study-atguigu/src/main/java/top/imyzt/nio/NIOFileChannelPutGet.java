package top.imyzt.nio;

import java.nio.ByteBuffer;

/**
 * NIO Buffer写入与读取是对应的
 * @author imyzt
 * @date 2021/06/06
 * @description 写入与读取是对应的
 */
public class NIOFileChannelPutGet {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        byteBuffer.putInt(1);
        byteBuffer.putLong(11111111);
        byteBuffer.putChar('A');
        byteBuffer.putShort((short) 3);

        byteBuffer.flip();

        // 顺序需要对应, 不然会报错或类型转换导致数据错误
        // BufferOverflowException
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());

    }
}