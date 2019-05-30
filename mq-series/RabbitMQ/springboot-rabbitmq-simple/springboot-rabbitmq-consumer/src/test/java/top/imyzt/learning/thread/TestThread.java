package top.imyzt.learning.thread;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.tomcat.util.threads.ScheduledThreadPoolExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2019/5/27
 * @description TestThread
 */
public class TestThread {

    public static void main(String[] args) {

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 5, 0L,
                TimeUnit.MINUTES, new LinkedBlockingQueue<>(3), new ThreadPoolExecutor.AbortPolicy());

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("example-scheduled-%d").daemon(true).build());

        for (int i = 0; i < 11; i++) {
            poolExecutor.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }

        poolExecutor.shutdown();

    }
}
