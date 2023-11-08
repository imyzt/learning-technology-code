package _02_completablefuture_create;

import top.imyzt.learning.jdk.utils.CommonUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步任务
 */
public class RunAsyncDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CommonUtils.print("main start");

        CompletableFuture.runAsync((() -> {
            CommonUtils.print("start");
            CommonUtils.sleepSecond(1);
            CommonUtils.print("end");
        }));

        CommonUtils.print("not blocked, main continue");
        CommonUtils.sleepSecond(1);
        CommonUtils.print("main end");

        //2023-11-08T22:17:22.139006 |  1 | main | main start
        //2023-11-08T22:17:22.180557 |  1 | main | not blocked, main continue
        //2023-11-08T22:17:22.181290 | 17 | ForkJoinPool.commonPool-worker-1 | start
        //2023-11-08T22:17:23.182381 | 17 | ForkJoinPool.commonPool-worker-1 | end
        //2023-11-08T22:17:23.182381 |  1 | main | main end


        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CompletableFuture<String> newsFuture = CompletableFuture
                .supplyAsync(() -> CommonUtils.readFiles("news.txt"), executorService);
        System.out.println(newsFuture.get());


        CompletableFuture.supplyAsync(() -> {
                    return CommonUtils.readFiles("filter_words.txt");
                }, executorService).thenApply(words -> {
                    return Arrays.stream(words.split(",")).toList();
                })
                .thenAccept(filterWords -> {
                    CommonUtils.print(filterWords.toString());
                });

        executorService.shutdown();
        CommonUtils.sleepSecond(2);
    }

}
