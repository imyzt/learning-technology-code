package top.imyzt.learning.liteflowdemo;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import top.imyzt.learning.liteflowdemo.context.StateContext;
import top.imyzt.learning.liteflowdemo.context.SwitchContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;

/**
 * liteflow-demo
 * @author imyzt
 */
@SpringBootApplication
public class LiteflowDemoApplication {

    private static final DelayQueue<DelayedTask> queue = new DelayQueue<>();

    public static void addTask(DelayedTask task) {
        queue.add(task);
    }

    @Resource
    private FlowExecutor flowExecutor;

    public static void main(String[] args) {
        SpringApplication.run(LiteflowDemoApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws InterruptedException {
        StateContext stateContext = new StateContext();

        // 信号
        CountDownLatch latch = new CountDownLatch(2);

        SwitchContext context = new SwitchContext(null, "b");
        LiteflowResponse response = flowExecutor.execute2Resp("chain2", "arg", context, stateContext);
        System.out.println(response);

        // 消费线程
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DelayedTask task = queue.take(); // 阻塞直到到期
                    System.out.println("执行：" + task.getMsg() + " @ " + System.currentTimeMillis());
                    flowExecutor.execute2Resp("chain2", "arg", context, stateContext);

                    latch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        latch.await();
    }


}
