package top.imyzt.learning.itcast.juc.lock.alternate;

/**
 * @author imyzt
 * @date 2022/08/08
 * @description 根据Synchronized实现交替执行
 */
public class AlternateOutputOfSynchronized {

    public static void main(String[] args) {

        final WaitNotify waitNotify = new WaitNotify(1, 10);

        new Thread(() -> waitNotify.print("a", 1, 2), "t1").start();
        new Thread(() -> waitNotify.print("b", 2, 3), "t2").start();
        new Thread(() -> waitNotify.print("c", 3, 1), "t3").start();

    }
}

class WaitNotify {

    /**
     * 等待标记
     */
    private int flag;

    /**
     * 循环次数
     */
    private final int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(String content, int waitFlag, int nextFlag) {
        for (int i = 0; i < this.loopNumber; i++) {
            synchronized (this) {
                while (waitFlag != this.flag) {
                    try {
                        // 如果不是当前标记, 则当前线程进入等待
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.print(content);
                // 将标记改为下一个线程
                this.flag = nextFlag;
                // 唤醒所有线程
                this.notifyAll();
            }
        }
    }
}