package top.imyzt.learning.nio.c1;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author imyzt
 * @date 2022/05/22
 * @description 使用transferTo拷贝文件, 零拷贝
 */
@Slf4j
public class TestFileChannelTransferTo {

    public static void main(String[] args) {

        Stopwatch started = Stopwatch.createStarted();
        try (
                FileChannel from = new FileInputStream("/Users/imyzt/logs/rocketmqlogs/rocketmq_client.log.2").getChannel();
                FileChannel to = new FileOutputStream("/tmp/to.log.3").getChannel();
        ) {

            // transferTo效率高, 使用操作系统零拷贝优化, 2g数据限制, 超过需要使用下面的代码进行处理多次拷贝
            long size = from.size();

            // left 代表, 还剩多少字节
            // transferSize 代表本次拷贝的字节
            long transferSize;
            for (long left = size; left > 0; left = left - transferSize) {
                log.info("position: {}, left: {}", (size + left), left);
                transferSize = from.transferTo((size - left), left, to);
            }
        } catch (IOException e) {
            log.error("transferErr", e);
        }

        System.out.println(started.toString());

    }
}