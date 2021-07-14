package top.imyzt.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author imyzt
 * @date 2021/06/06
 * @description 直接在堆外内存操作文件, 不用操作系统拷贝数据到JVM内存再操作
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("/tmp/1.txt", "rw");

        // 获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /*
        mode -> 读写模式
        position -> 直接修改的起始位置
        size -> 映射到内存的大小(不是索引位置), 即 -> 将1.txt中的多少个字节映射到内存

        如下配置(0,5)表示可以修改的范围是[0-5)
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) 9);
        // 索引越界
        // mappedByteBuffer.put(5, (byte) 1);

        randomAccessFile.close();

        System.out.println("完成");

    }
}