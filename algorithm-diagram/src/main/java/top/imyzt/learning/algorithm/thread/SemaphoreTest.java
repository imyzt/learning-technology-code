package top.imyzt.learning.algorithm.thread;


import java.util.concurrent.Semaphore;

/**
 * @author imyzt
 * @date 2024/07/08
 * @description 使用信号量实现三个线程交替打印
 */
public class SemaphoreTest {

    public static void main(String[] args) {

        Semaphore first = new Semaphore(0);
        Semaphore second = new Semaphore(0);
        Semaphore third = new Semaphore(0);

        Thread t1 = new Thread(() -> {
            for (; ; ) {
                try {
                    System.out.print('1');
                    Thread.sleep(1000);
                    // 首先唤醒第二个
                    second.release();
                    // 休眠等待唤醒
                    first.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "print 1");

        Thread t2 = new Thread(() -> {
            for (; ; ) {
                try {
                    // 第二个首先是等待
                    second.acquire();
                    System.out.print('2');
                    Thread.sleep(1000);
                    // 唤醒第三个
                    third.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "print 2");

        Thread t3 = new Thread(() -> {
            for (; ; ) {
                try {
                    // 第二个首先是等待
                    third.acquire();
                    System.out.print('3');
                    Thread.sleep(1000);
                    // 唤醒第一个
                    first.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "print 3");

        t3.start();
        t1.start();
        t2.start();
    }
}
