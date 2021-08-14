package top.imyzt.learning.mdc.childthread.newpool;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2021/08/14
 * @description 子线程模式-通过new对象手动提交线程任务保留mdc
 */
@Slf4j
@RestController
public class NewPoolDemo {

    private static final ThreadPoolTaskExecutor THREAD_POOL_EXECUTOR =
            new MdcThreadPoolTaskExecutor(null, 3, 10,
                    1, TimeUnit.MINUTES,
                    10000, "new-pool-demo");

    @GetMapping("new-pool-demo")
    public void test() {
        MDC.put("testMdcId", "abc");
        log.info("外层日志有MDC吗?");
        THREAD_POOL_EXECUTOR.execute(() -> log.info("内层日志有MDC吗?"));
        CompletableFuture.runAsync(() -> log.info("CompletableFuture.runAsync()内层日志有MDC吗?"), THREAD_POOL_EXECUTOR);
    }

}