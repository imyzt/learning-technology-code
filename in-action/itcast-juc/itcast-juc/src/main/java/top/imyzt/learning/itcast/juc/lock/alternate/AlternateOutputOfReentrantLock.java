package top.imyzt.learning.itcast.juc.lock.alternate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author imyzt
 * @date 2022/08/08
 * @description 使用ReentrantLock实现交替执行
 */
public class AlternateOutputOfReentrantLock {

    public static void main(String[] args) throws InterruptedException {

        AwaitSignal awaitSignal = new AwaitSignal(10);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(() -> awaitSignal.print("a", a, b), "t1").start();
        new Thread(() -> awaitSignal.print("b", b, c), "t2").start();
        new Thread(() -> awaitSignal.print("c", c, a), "t3").start();

        TimeUnit.SECONDS.sleep(1);
        awaitSignal.lock();
        try {
            System.out.println("开始...");
            // 由主线程主动唤醒某个线程, 使整个交替执行开始运转
            a.signal();
        } finally {
            awaitSignal.unlock();
        }
    }
}

class AwaitSignal extends ReentrantLock {

    private final int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String content, Condition current, Condition next) {
        for (int i = 0; i < this.loopNumber; i++) {
            this.lock();
            try {
                // 当前线程等待
                current.await();
                System.out.print(content);
                // 唤醒下一个线程执行
                next.signal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                this.unlock();
            }
        }
    }
}