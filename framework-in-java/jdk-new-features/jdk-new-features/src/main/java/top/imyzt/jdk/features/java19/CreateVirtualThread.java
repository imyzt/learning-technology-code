package top.imyzt.jdk.features.java19;


import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2023/12/18
 * @description VirtualThread 2
 */
public class CreateVirtualThread {
    public static void main(String[] args) throws InterruptedException {

        Runnable r = () -> System.out.println(Thread.currentThread().getName() + " --- 执行了");

        // 创建虚拟线程, 方式1
        Thread.startVirtualThread(r);

        Thread virtualThread = Thread.ofVirtual().name("virtual-thread-").unstarted(r);
        virtualThread.start();
        System.out.println("是虚拟线程吗? " + virtualThread.isVirtual());

        Thread platformThread = Thread.ofPlatform().priority(0).daemon(true).name("platform-thread-").unstarted(r);
        platformThread.start();
        System.out.println("是虚拟线程吗? " + platformThread.isVirtual());

        // --- 执行了
        //virtual-thread- --- 执行了
        //是虚拟线程吗? true
        //platform-thread- --- 执行了
        //是虚拟线程吗? false

        // 主线程休眠
        TimeUnit.SECONDS.sleep(1);
    }
}
