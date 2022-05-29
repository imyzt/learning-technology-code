package top.imyzt.learning.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description ByteBuffer
 */
@Slf4j
public class TestByteBuffer {

    public static void main(String[] args) throws IOException {

        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {

                // 从channel读取数据, 写入buffer
                int len = channel.read(buffer);
                log.info("读取字节数: {}", len);
                if (len == -1) {
                    break;
                }

                // 切换读模式
                buffer.flip();

                while (buffer.hasRemaining()) {
                    // 读取数据
                    char data = (char) buffer.get();
                    log.info("实际数据: {}", data);
                }

                // 切换为写模式(从头写)
                buffer.clear();
                // 将为读完的部分先前压缩, 然后切换至写模式(从上次未读完的地方开始写), 避免丢数据
                //  buffer.compact();
            }
        }
    }
}