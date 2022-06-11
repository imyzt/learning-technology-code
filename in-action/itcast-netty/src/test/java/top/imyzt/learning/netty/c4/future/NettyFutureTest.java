package top.imyzt.learning.netty.c4.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 描述信息
 */
@Slf4j
public class NettyFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // netty sync 同步阻塞等待结果, 和jdk future差不多
        sync();

        // netty asnyc, 异步非阻塞获取结果(通过回调), 优于jdk future
        async();

    }

    private static void sync() throws InterruptedException, ExecutionException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        EventLoop eventLoop = group.next();

        Future<Integer> future = eventLoop.submit(() -> {
            log.info("执行计算");
            Thread.sleep(1000);
            return 100;
        });

        log.info("等待结果");
        log.info("结果是: {}", future.get());

    }

    private static void async() {
        NioEventLoopGroup group = new NioEventLoopGroup();

        EventLoop eventLoop = group.next();

        Future<Integer> future = eventLoop.submit(() -> {
            log.info("执行计算");
            Thread.sleep(1000);
            return 100;
        });

        future.addListener(future1 -> log.info("获取结果: {}", future1.getNow()));
    }
}