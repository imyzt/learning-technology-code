package top.imyzt.nio;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 从文件中读取数据
 * @author imyzt
 * @date 2021/06/06
 * @description Nio读文件
 */
public class NIOFileChannelRead {

    public static void main(String[] args) throws IOException {

        File file = new File("/tmp/nioFileOut.txt");
        FileInputStream fis = new FileInputStream(file);
        FileChannel fileChannel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        fileChannel.read(byteBuffer);

        byteBuffer.flip();

        System.out.println(new String(byteBuffer.array()));

        fis.close();

    }
}