package top.imyzt.jdk.features.jdk9;


import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2023/12/16
 * @description java 9 在接口中可以定义 private 方法, 只可在本接口中调用
 */
public interface InterfaceFuture {

    void foo();

    /**
     * java 8
     */
    default void foo1() throws InterruptedException {
        sleep();
    }

    /**
     * java 8
     */
    static void foo2() throws InterruptedException {
        sleep2();
    }

    /**
     * java 9
     */
    private void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * java 9
     */
    private static void sleep2() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
    }
}
