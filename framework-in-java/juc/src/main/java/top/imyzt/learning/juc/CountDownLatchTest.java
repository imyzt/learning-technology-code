package top.imyzt.learning.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 通过计数法（倒计时器），让一些线程堵塞直到另一个线程完成一系列操作后才被唤醒；
 * 该工具通常用来控制线程等待，它可以让某一个线程等待直到倒计时结束，再开始执行。
 * 具体可以使用countDownLatch.await()来等待结果。多用于多线程信息汇总。
 * @author imyzt
 * @date 2024/03/10
 * @description CountDownLatch 是一次性的, 一个或者多个线程，等待其他多个线程完成某件事情之后才能执行
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        int count = 100;
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(40);
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        countDownLatch.await();
        System.out.println("执行完毕!" + (System.currentTimeMillis() - start));
    }
}
