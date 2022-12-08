package top.imyzt.learning.dp.flow.jdk9;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

/**
 * @author imyzt
 * @date 2022/12/08
 * @description 数据处理者
 */
public class DataProcessor extends SubmissionPublisher<String> implements Processor<String, String> {

    private Subscription subscription;

    private final String name;

    private final CountDownLatch countDownLatch;

    public DataProcessor(String name, CountDownLatch countDownLatch) {
        this.name = name;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onSubscribe(Subscription subscription) {

        this.subscription = subscription;

        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {

        System.out.printf("[处理器(%s)]接收消息---------->%s%n", this.name, item);

        this.countDownLatch.countDown();

        String newItem = "[处理器(%s)加工后的数据: %s".formatted(this.name, item);

        // 重新将消息发出去
        this.submit(newItem);

        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("[处理器]数据接收出现异常");
    }

    @Override
    public void onComplete() {
        System.out.println("[处理器]数据处理完毕");
        this.close();
    }
}