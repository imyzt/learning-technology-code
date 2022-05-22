package top.imyzt.learning.netty.c2.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description Nio Client
 */
@Slf4j
public class Client {

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8888));

        System.out.println("waiting...");

        socketChannel.write(StandardCharsets.UTF_8.encode("hello"));
    }
}