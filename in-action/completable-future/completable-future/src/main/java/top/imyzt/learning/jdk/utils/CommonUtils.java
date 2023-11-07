package top.imyzt.learning.jdk.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2023/11/07
 * @description 描述信息
 */
public class CommonUtils {

    public static String readFiles(String path) {
        try {
            return Files.readString(Paths.get(CommonUtils.class.getClassLoader().getResource(path).getPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void sleepMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {

        }
    }

    public static void sleepSecond(long second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {

        }
    }

    public static void print(String message) {
        String msg = new StringJoiner(" | ")
                .add(LocalDateTime.now().toString())
                .add(String.format("%2d", Thread.currentThread().getId()))
                .add(Thread.currentThread().getName())
                .add(message)
                .toString();
        System.out.println(msg);
    }

    public static void main(String[] args) {
        print("start");
        sleepMillis(1);
        sleepSecond(1);
        print(readFiles("news.txt"));
        print("end");
    }
}