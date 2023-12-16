package top.imyzt.learning.algorithm.chapter02;

import cn.hutool.core.io.FileUtil;
import top.imyzt.learning.algorithm.utils.ListUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author imyzt
 * @date 2021/04/24
 * @description 选择排序
 */
public class SelectionSort {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1024), new ThreadPoolExecutor.CallerRunsPolicy());

        // 启动所有核心线程(预热)
        threadPoolExecutor.prestartAllCoreThreads();
        // 启动一个核心线程
        threadPoolExecutor.prestartCoreThread();

        // 默认情况下构造器中的keepAliveTime指定的是非核心线程的空闲时间, 通过如下方法, 可以允许核心线程超时
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        // ⭐️ 动态线程池必备方法
        // 启动后, 设置核心线程数量
        threadPoolExecutor.setCorePoolSize(3);
        // 启动后, 设置最大线程数量
        threadPoolExecutor.setMaximumPoolSize(10);
        // 已执行完的任务总数
        threadPoolExecutor.getTaskCount();
        // 获取工作队列剩余数量
        threadPoolExecutor.getQueue().remainingCapacity();
    }

    public static ArrayList<Integer> selectionSort(List<Integer> list) {
        ArrayList<Integer> newList = new ArrayList<>(list.size());
        for (int i = list.size() - 1; i >= 0; i--) {
            int smallestIdx = findSmallestIdx(list);
            newList.add(list.remove(smallestIdx));
        }
        return newList;
    }

    private static int findSmallestIdx(List<Integer> list) {
        int smallest = list.get(0);
        int smallestIdx = 0;
        for (int j = list.size() - 1; j >= 0; j--) {
            Integer curr = list.get(j);
            if (curr < smallest) {
                smallest = curr;
                smallestIdx = j;
            }
        }
        return smallestIdx;
    }
}
