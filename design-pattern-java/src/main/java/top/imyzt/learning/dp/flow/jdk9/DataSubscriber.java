package top.imyzt.learning.dp.flow.jdk9;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2022/12/08
 * @description 数据消费者
 */
public class DataSubscriber implements Subscriber<String> {

    private Subscription subscription;

    /**
     * 当前处理数量
     */
    private int reqHandle;

    /**
     * 最大处理上线
     */
    private final int maxReqHandle;

    private final String name;

    private final CountDownLatch countDownLatch;

    public DataSubscriber(int maxReqHandle, String name, CountDownLatch countDownLatch) {
        this.maxReqHandle = maxReqHandle;
        this.name = name;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        // 通过subscription和发布者保持订阅关系, 并用他来给发布者反馈
        this.subscription = subscription;
        // 请求1个数据
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {

        System.out.printf("[订阅者(%s)]接收消息--------->%s%n", this.name, item);

        try {
            // 慢速接收, 模拟背压
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        reqHandle++;
        countDownLatch.countDown();

        // 消费后, 再取1个数据
        this.subscription.request(1);

        if (reqHandle > maxReqHandle) {
            // 如果不想接收消息, 可以调用停止接收
            this.subscription.cancel();
            System.out.printf("[订阅者(%s)]超过最大处理量[%s], 停止处理%n", this.name, maxReqHandle);
        }
    }

    @Override
    public void onError(Throwable throwable) {

        System.out.printf("[订阅者(%s)]数据接收出现异常, %s%n", this.name, throwable);

        this.subscription.cancel();

    }

    @Override
    public void onComplete() {
        // 当发布者发出的数据都被消费了, 且发布者关闭后, 会回调此方法
        System.out.println("[订阅者]数据接收完毕");
    }
}