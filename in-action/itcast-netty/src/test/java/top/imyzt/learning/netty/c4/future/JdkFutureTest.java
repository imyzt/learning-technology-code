package top.imyzt.learning.netty.c4.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author imyzt
 * @date 2022/06/11
 * @description 描述信息
 */
@Slf4j
public class JdkFutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // jdk future 同步阻塞获取结果
        sync();
    }

    private static void sync() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future<Integer> future = executorService.submit(() -> {
            log.info("执行计算");
            Thread.sleep(1000);
            return 10;
        });

        log.info("等待执行结果");
        log.info("结果是: {}", future.get());

        // 主线程阻塞等待数据, EventLoop线程执行计算
        //11:35:16.811 [pool-1-thread-1] INFO top.imyzt.learning.netty.c4.future.JdkFutureTest - 执行计算
        //11:35:16.811 [main] INFO top.imyzt.learning.netty.c4.future.JdkFutureTest - 等待执行结果
        //11:35:17.821 [main] INFO top.imyzt.learning.netty.c4.future.JdkFutureTest - 结果是: 10
    }
}