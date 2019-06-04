package top.imyzt.learning.security.demo.web.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2019/6/4
 * @description QueueListener
 */
@Component
@Slf4j
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 1,
            0, TimeUnit.MINUTES, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        poolExecutor.submit(() -> {
            while (true) {
                // 不为空, 消费
                if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {

                    String orderNumber = mockQueue.getCompleteOrder();
                    log.info("返回订单处理结果: {}",orderNumber );

                    deferredResultHolder.getMap().get(orderNumber).setResult("place order success");

                    // 模拟消息队列消费后, 避免死循环
                    mockQueue.setCompleteOrder(null);
                } else {
                    TimeUnit.SECONDS.sleep(1);
                }
            }
        });
    }
}
