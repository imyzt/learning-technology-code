package top.imyzt.learning.jdk;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author imyzt
 * @date 2022/10/20
 * @description 基本方法使用
 */
public class CompletableFuture01 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        CompletableFuture
                // 静态方法, 无返回值
                .runAsync((new LongTask(1, "runAsync")));

        CompletableFuture<String> supplyAsync = CompletableFuture
                // 静态方法, 有返回值
                .supplyAsync(() -> {
                    new LongTask(1, "supplyAsync").run();
                    return "supplyAsync";
                });
        System.out.println(supplyAsync.get());


        System.out.println();
        CompletableFuture
                .runAsync(new LongTask(1, "a"))
                // 在执行后, 获取到执行结果再进行操作
                .thenApply(result -> "aa")
                // 拿到执行结果, 再组装一个CompletableFuture, 和flatMap类似
                .thenCompose(result -> CompletableFuture.supplyAsync(() -> result + result))
                // 无法拿到执行结果, 只能执行runnable, 且后续动作再无法拿到之前的result
                .thenRun(new LongTask(1, "bbbb"))
                // 拿到执行结果, 执行一个Consumer
                .thenAccept(result -> System.out.println("result=" + result));


        Thread.sleep(32000);
    }
}