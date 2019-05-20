package top.imyzt.nio.chat;

import java.io.IOException;

/**
 * @author imyzt
 * @date 2019/5/20
 * @description 客户端A
 */
public class AClient {
    public static void main(String[] args) throws IOException {
        NioClient aClient = new NioClient();
        aClient.start("A-Client");
    }
}
