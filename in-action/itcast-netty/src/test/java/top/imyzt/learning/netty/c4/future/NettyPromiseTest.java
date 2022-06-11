package top.imyzt.learning.netty.c4.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 描述信息
 */
@Slf4j
public class NettyPromiseTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new NioEventLoopGroup().next();

        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {

            log.info("开始计算");

            try {
                int i = 1 / 0;

                Thread.sleep(1000);

                promise.setSuccess(100);
            } catch (Exception e) {
                promise.setFailure(e);
            }
        }).start();


        log.info("等待结果...");
        log.info("结果是: {}", promise.get());
    }
}