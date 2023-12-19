package top.imyzt.jdk.features.java19;


import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author imyzt
 * @date 2023/12/18
 * @description VirtualThread 1
 */
public class VirtualThread {

    public static void main(String[] args) throws InterruptedException {

        // 通过线程池创建虚拟线程池
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10000000).forEach(i -> {
                executor.submit(() -> {
                    try {
                        Thread.sleep(Duration.ofSeconds(1));
                        System.out.println("执行任务: " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
