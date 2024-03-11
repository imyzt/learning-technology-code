package top.imyzt.learning.juc;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author imyzt
 * @date 2024/03/10
 * @description CyclicBarrier 可重复使用, 多个线程互相等待，直到到达同一个同步点，再继续一起执行
 */
public class CyclicBarrierTest {

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        // 大家一起等吃饭, 10人一桌, 满10人就开吃
        // eat(cyclicBarrier);
        // 同上,但第五人有事,没等后面的人了, 那么前面4个看第五个开吃, 自己也开始吃了, 后面的6~10来的时候, 看前面的开始吃了,自己也一来就吃
        // eat2(cyclicBarrier);
        // 同一, 但第五人有事,只等了一秒,就开吃了. 前面4个看第五个开吃, 自己也开吃了, 后面的来的时候, 看前面开吃, 自己也一来就吃了
        eat3(cyclicBarrier);

        TimeUnit.SECONDS.sleep(30);
    }

    private static void eat(CyclicBarrier cyclicBarrier) {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                try {
                    long starTime = System.currentTimeMillis();
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println(Thread.currentThread().getName() + ",上桌开始等待");
                    cyclicBarrier.await();
                    long endTime = System.currentTimeMillis();
                    System.out.println(Thread.currentThread().getName() + ",sleep:" + finalI + " 等待了" + (endTime - starTime) + "(ms),开始吃饭了！");
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static void eat2(CyclicBarrier cyclicBarrier) {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                long starTime = System.currentTimeMillis();
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println(finalI + ",上桌开始等待");

                    if (finalI == 5) {
                        System.out.println("我是5, 不等了, 开吃");
                        Thread.currentThread().interrupt();
                    }

                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + ",sleep:" + finalI + " 等待了" + (endTime - starTime) + "(ms),开始吃饭了！");
            });
        }
    }

    private static void eat3(CyclicBarrier cyclicBarrier) {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            CompletableFuture.runAsync(() -> {
                long starTime = System.currentTimeMillis();
                try {
                    TimeUnit.SECONDS.sleep(finalI);
                    System.out.println(finalI + ",上桌开始等待");
                    if (finalI == 5) {
                        System.out.println("我是5, 我待会有事, 就只等1秒, 就先开吃了");
                        cyclicBarrier.await(1, TimeUnit.SECONDS);
                    } else {
                        cyclicBarrier.await();
                    }
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + ",sleep:" + finalI + " 等待了" + (endTime - starTime) + "(ms),开始吃饭了！");
            });
        }
    }
}
