package _01_future_vs_completablefuture;

import top.imyzt.learning.jdk.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author imyzt
 * @date 2023/11/07
 * @description 使用原始方式
 */
public class FutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        // 读取敏感词
        Future<List<String>> filterWordsFuture = executorService.submit(() -> {
            return Arrays.stream(CommonUtils.readFiles("filter_words.txt").split(",")).toList();
        });

        // 读取新闻
        Future<String> newsFuture = executorService.submit(() -> {
            return CommonUtils.readFiles("news.txt");
        });

        // 替换敏感词
        Future<String> newsWord = executorService.submit(() -> {
            List<String> filterWords = filterWordsFuture.get();
            String news = newsFuture.get();
            for (String filterWord : filterWords) {
                if (news.indexOf(filterWord) > 0
                        || news.indexOf(filterWord.toLowerCase()) > 0
                        || news.indexOf(filterWord.toUpperCase()) > 0) {
                    news = news.replace(filterWord, "**");
                    news = news.replace(filterWord.toLowerCase(), "**");
                    news = news.replace(filterWord.toUpperCase(), "**");
                }
            }
            return news;
        });

        // 输出内容
        CommonUtils.print(newsWord.get());

        System.exit(0);
    }
}