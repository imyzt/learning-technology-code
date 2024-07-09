package top.imyzt.learning.algorithm.thread;

import java.util.concurrent.CompletableFuture;

/**
 * @author imyzt
 * @date 2023/07/08
 * @description 3个线程交替运行
 */
public class ThreadAlternately {

    public static void main(String[] args) throws InterruptedException {

        SimpleThread simpleThread = new SimpleThread(1, 50);
        CompletableFuture.runAsync(() -> simpleThread.print("A", 1, 2));
        CompletableFuture.runAsync(() -> simpleThread.print("B", 2, 3));
        CompletableFuture.runAsync(() -> simpleThread.print("C", 3, 1));

        Thread.sleep(1000);
    }

}
class SimpleThread {

    private int flag;

    private final int loopNum;

    public SimpleThread(int flag, int loopNum) {
        this.flag = flag;
        this.loopNum = loopNum;
    }

    public void print(String printStr, int currFlag, int nextFlag) {

        for (int i = 0; i < loopNum; i++) {
            synchronized (this) {
                while (flag != currFlag) {
                    try {
                        // System.out.println("不等于, 休息" + printStr);
                        this.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(printStr);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

}
