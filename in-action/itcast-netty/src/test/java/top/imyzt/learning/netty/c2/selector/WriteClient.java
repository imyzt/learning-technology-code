package top.imyzt.learning.netty.c2.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description Nio Client
 */
@Slf4j
public class WriteClient {

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8888));

        int count = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            count += socketChannel.read(buffer);
            System.out.println(count);
            buffer.clear();
        }
    }
}