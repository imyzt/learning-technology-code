package top.imyzt.learning.juc;


import java.util.concurrent.Semaphore;

/**
 * @author imyzt
 * @date 2024/03/10
 * @description Semaphore 信号量
 */
public class SemaphoreTest {

    public static class Runner extends Thread {

        private final int num;
        private final Semaphore semaphore;

        public Runner(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println(getName() + ": " + num);
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(1);

        for (int i = 1; i <= 10; i++) {
            Thread thread1 = new Runner(i, semaphore);
            thread1.start();
        }

    }
}
