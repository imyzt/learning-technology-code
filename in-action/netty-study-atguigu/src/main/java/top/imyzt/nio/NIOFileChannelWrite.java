package top.imyzt.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 写入数据到文件
 * @author imyzt
 * @date 2021/06/06
 * @description Channel -> FileChannel
 */
public class NIOFileChannelWrite {

    public static void main(String[] args) throws IOException {

        // 获取channel
        FileOutputStream fos = new FileOutputStream("/tmp/nioFileOut.txt");
        // 利用流, 拿到Channel -> FileChannelImpl
        FileChannel fileChannel = fos.getChannel();

        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 写入数据到缓冲区
        String str = "hello world!";
        byteBuffer.put(str.getBytes());
        // 读写切换
        byteBuffer.flip();

        // 写入缓冲区数据到channel
        fileChannel.write(byteBuffer);

        // 关闭最底层的流对象
        fos.close();
    }
}