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
                // 如遇异常, 将异常包装后在get方法原样返回堆栈
                promise.setFailure(e);
            }
        }).start();

        log.info("等待结果...");
        // 异步等待结果
        promise.addListener(future -> log.info("回调监听获取结果: {}", future.getNow()));
        // 同步阻塞等待结果
        log.info("同步阻塞获取结果: {}", promise.get());
    }
}