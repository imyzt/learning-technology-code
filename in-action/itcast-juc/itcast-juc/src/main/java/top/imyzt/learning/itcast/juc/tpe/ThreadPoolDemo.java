package top.imyzt.learning.itcast.juc.tpe;

import lombok.extern.slf4j.Slf4j;
import top.imyzt.learning.itcast.juc.tpe.reject.CallerRunsPolicy;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2022/08/22
 * @description 描述信息
 */
@Slf4j
public class ThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {

        ThreadPool tp = new ThreadPool(2, 1, TimeUnit.SECONDS, 10, new CallerRunsPolicy());
        for (int i = 0; i < 15; i++) {
            int j = i;
            tp.execute(() -> {
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("{}", j);
            });
        }
        Thread.sleep(1000);
    }
}
