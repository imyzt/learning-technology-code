package top.imyzt.nio.chat;

import java.io.IOException;

/**
 * @author imyzt
 * @date 2019/5/20
 * @description 客户端B
 */
public class BClient {
    public static void main(String[] args) throws IOException {
        NioClient bClient = new NioClient();
        bClient.start("B-Client");
    }
}
