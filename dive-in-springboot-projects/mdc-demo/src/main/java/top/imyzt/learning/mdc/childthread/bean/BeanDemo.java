package top.imyzt.learning.mdc.childthread.bean;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author imyzt
 * @date 2021/08/14
 * @description 子线程模式-通过bean手动提交线程任务保留mdc
 */
@Slf4j
@RestController
public class BeanDemo {

    @Resource(name = "beanDemoExec")
    private Executor executor;

    @GetMapping("bean-demo")
    public void test() {
        MDC.put("testMdcId", "abc");
        log.info("外层日志有MDC吗?");
        executor.execute(() -> log.info("内层日志有MDC吗?"));
        CompletableFuture.runAsync(() -> log.info("CompletableFuture.runAsync()内层日志有MDC吗?"), executor);
    }

    @Bean("beanDemoExec")
    public AsyncTaskExecutor inviteHelp() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("bean-demo-exec");
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(300);
        threadPoolTaskExecutor.setCorePoolSize(2);
        // 增加任务装饰器
        threadPoolTaskExecutor.setTaskDecorator(new MdcTaskDecorator());
        return threadPoolTaskExecutor;
    }

}