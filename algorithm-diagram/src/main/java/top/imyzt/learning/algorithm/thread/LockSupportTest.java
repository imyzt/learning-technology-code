package top.imyzt.learning.algorithm.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * @author imyzt
 * @date 2023/07/08
 * @description 使用Lock工具类交替执行
 */
public class LockSupportTest {

    public static void main(String[] args) {

        Thread[] threads = new Thread[2];
        threads[0] = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                System.out.println("A");
                LockSupport.unpark(threads[1]);
                LockSupport.park();
            }
        });

        threads[1] = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                LockSupport.park();
                System.out.println("B");
                LockSupport.unpark(threads[0]);
            }
        });

        threads[0].start();
        threads[1].start();
    }
}
class SimpleThread2 {

}