package top.imyzt.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * transferFrom做了简单封装, 代码位于sun.nio.ch.FileChannelImpl#transferFromFileChannel(sun.nio.ch.FileChannelImpl, long, long)
 * @author imyzt
 * @date 2021/06/06
 * @description 利用NIO拷贝文件
 */
public class NIOFileChannelCopyFile {

    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream("/tmp/1.txt");
        FileChannel source = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("/tmp/3.txt");
        FileChannel target = fos.getChannel();

        target.transferFrom(source, 0, source.size());

    }
}