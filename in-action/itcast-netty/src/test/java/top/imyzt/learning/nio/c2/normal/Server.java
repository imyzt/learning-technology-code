package top.imyzt.learning.nio.c2.normal;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description NIO server
 */
@Slf4j
public class Server {

    public static void main(String[] args) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(10);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        // 非阻塞模式
        serverSocketChannel.configureBlocking(false);

        ArrayList<@Nullable SocketChannel> channels = Lists.newArrayList();

        while (true) {

            // 阻塞等待客户端连接, 线程停止运行
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                log.info("connected...{}", socketChannel);
                // 非阻塞模式
                socketChannel.configureBlocking(false);
                channels.add(socketChannel);
            }

            for (SocketChannel channel : channels) {
                // 阻塞防范, 线程停止运行
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    log.info("receive data: {}", StandardCharsets.UTF_8.decode(buffer).toString());
                    buffer.clear();
                    log.info("after read...{}", channel);
                }
            }
        }

    }
}