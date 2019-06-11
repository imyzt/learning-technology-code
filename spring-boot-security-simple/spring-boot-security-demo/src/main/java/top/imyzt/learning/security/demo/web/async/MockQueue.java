package top.imyzt.learning.security.demo.web.async;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2019/6/4
 * @description 模拟的队列消息对象
 */
@Data
@Slf4j
@Component
public class MockQueue {

    private String placeOrder;

    private String completeOrder;

    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.MINUTES, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.AbortPolicy());

    /**
     * 模拟消息入队, 暂停一秒, 然后消费的过程
     */
    void setPlaceOrder(String placeOrder) {

        poolExecutor.submit(() -> {
            log.info("接到下单请求: {}", placeOrder);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            this.completeOrder = placeOrder;

            log.info("下单请求处理完毕: {}", placeOrder);
        });
    }
}
