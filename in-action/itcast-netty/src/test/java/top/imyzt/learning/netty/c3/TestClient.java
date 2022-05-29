package top.imyzt.learning.netty.c3;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2022/05/29
 * @description 多路复用客户端
 */
@Slf4j
public class TestClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 9999));
        sc.write(StandardCharsets.UTF_8.encode("1234567890ab"));
        TimeUnit.SECONDS.sleep(100);
    }
}