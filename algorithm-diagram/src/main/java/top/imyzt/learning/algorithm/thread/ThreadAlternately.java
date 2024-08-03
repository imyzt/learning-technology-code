package top.imyzt.learning.algorithm.thread;

import cn.hutool.system.UserInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author imyzt
 * @date 2023/07/08
 * @description 3个线程交替运行
 */
public class ThreadAlternately {

    public static void main(String[] args) throws InterruptedException {

        List<UserInfo1> s = new ArrayList<>();
        // UserInfo1 e = new UserInfo1();
        // s.add(e);

        List<String> collect = s.stream().map(UserInfo1::getName).filter(Objects::nonNull).collect(Collectors.toList());
        System.out.println(collect);

    }

}
@Data
class UserInfo1 {
    private String name;
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
