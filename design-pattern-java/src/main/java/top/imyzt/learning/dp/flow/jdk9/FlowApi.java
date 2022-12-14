package top.imyzt.learning.dp.flow.jdk9;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;

/**
 *
 * 1. API简单, 可以支持背压backpressure, 当接收者压力大时, Subscriber会将Publisher的数据缓冲在Subscription中, 默认长度为256
 * 2. 支持中间处理数据, {@link DataProcessor}
 *
 * @author imyzt
 * @date 2022/12/08
 * @description jdk9 观察者模式测试
 */
public class FlowApi {

    public static void main(String[] args) throws InterruptedException {

        int maxReqHandle = 10;
        int loop = maxReqHandle + 1;
        CountDownLatch countDownLatch = new CountDownLatch(loop * 2);

        // 1. 创建观察者
        Processor<String, String> process = new DataProcessor( "Processor", countDownLatch);
        Subscriber<String> subscriberA = new DataSubscriber(maxReqHandle, "AAA", countDownLatch);
        Subscriber<String> subscriberB = new DataSubscriber(maxReqHandle, "BBB", countDownLatch);


        // 2. 创建被观察者
        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {

            // 3. 发布者和订阅者绑定关系
            publisher.subscribe(process);
            process.subscribe(subscriberA);
            process.subscribe(subscriberB);

            for (int i = 0; i < loop; i++) {
                String message = "hello flow api " + i;
                System.out.println("[发布者]发布消息--------->" + message);
                publisher.submit(message);
            }

            // 4. 发布完成, 手动关闭发布者
            publisher.close();
        }

        countDownLatch.await();
        System.out.println("结束");

    }
}

