package top.imyzt.learning.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author imyzt
 * @date 2022/05/29
 * @description 描述信息
 */
@Slf4j
public class AioFileChannel {

    public static void main(String[] args) throws IOException {

        AsynchronousFileChannel channel = null;
        try {

            channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ);
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.info("read begin...");
            // 参数1, ByteBuffer
            // 参数2, 读取起始位置
            // 参数3, 附件
            // 参数4, 回调对象CompletionHandler
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    attachment.flip();
                    log.info("read completed..., {}, {}", result, StandardCharsets.UTF_8.decode(attachment));
                }
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
            log.info("read end...");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            if (channel != null) {
//                channel.close();
//            }
        }

        System.in.read();
    }
}