package top.imyzt.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author imyzt
 * @date 2021/05/09
 * @description BIO测试
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);

        while (true) {
            final Socket socket = serverSocket.accept();
            executorService.execute(() -> handler(socket));
        }

    }

    private static void handler(Socket socket) {
        System.out.println("新建连接, handlerThread={}" + Thread.currentThread().getName());
        try {
            InputStream inputStream = socket.getInputStream();

            byte[] bytes = new byte[1024];

            while (true) {
                int read = inputStream.read(bytes);
                if (read != -1) {
                    String msg = new String(bytes, 0, read);
                    System.out.println("收到消息: " + msg);
                    OutputStream outputStream = socket.getOutputStream();

                    String revertStr = revertStr(msg);
                    outputStream.write((revertStr + "\n").getBytes());
                } else {
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String revertStr(String msg) {
        StringBuilder sb = new StringBuilder();
        for (int length = msg.length(); length > 0; length--) {
            sb.append(msg.charAt(length-1));
        }
        return sb.toString();
    }
}