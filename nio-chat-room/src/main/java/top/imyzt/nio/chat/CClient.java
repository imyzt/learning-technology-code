package top.imyzt.nio.chat;

import java.io.IOException;

/**
 * @author imyzt
 * @date 2019/5/20
 * @description 客户端C
 */
public class CClient {
    public static void main(String[] args) throws IOException {
        NioClient cClient = new NioClient();
        cClient.start("C-Client");
    }
}
