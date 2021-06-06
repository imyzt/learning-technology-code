package top.imyzt.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author imyzt
 * @date 2021/06/06
 * @description 使用一个Buffer完成读取和写入动作, 通过clear和flip
 */
public class NIOFileChannelReadWrite {

    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream("/tmp/1.txt");
        FileChannel fileChannel01 = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("/tmp/2.txt");
        FileChannel fileChannel02 = fos.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true) {

            // 清空标记位
            byteBuffer.clear();

            // 读取, 写入到缓冲区
            int read = fileChannel01.read(byteBuffer);
            if (read == -1) {
                break;
            }

            // 翻转
            byteBuffer.flip();
            // 写入
            fileChannel02.write(byteBuffer);
        }

        fis.close();
        fos.close();
    }
}