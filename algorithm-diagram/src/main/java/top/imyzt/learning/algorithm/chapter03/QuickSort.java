package top.imyzt.learning.algorithm.chapter03;

import top.imyzt.learning.algorithm.chapter02.SelectionSort;
import top.imyzt.learning.algorithm.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2021/04/24
 * @description 快速排序
 */
public class QuickSort {

    public static void main(String[] args) {

        List<Integer> list = ListUtils.randomNotRepeatList(40000, 200000000);

        // 快排
        long quickStart = System.currentTimeMillis();
        System.out.println(quickSort(new ArrayList<>(list)));
        long quickEnd = System.currentTimeMillis();

        // 选择排序
        long selectionStart = System.currentTimeMillis();
        System.out.println(SelectionSort.selectionSort(new ArrayList<>(list)));
        long selectionEnd = System.currentTimeMillis();

        long jdkSortStart = System.currentTimeMillis();
        new ArrayList<>(list).sort(Integer::compareTo);
        long jdkSortEnd = System.currentTimeMillis();

        System.out.println("快排耗时: " + (quickEnd - quickStart));
        System.out.println("选择排序耗时: " + (selectionEnd - selectionStart));
        System.out.println("JDK排序耗时: " + (jdkSortEnd - jdkSortStart));

    }

    private static List<Integer> quickSort(List<Integer> list) {

        if (list.size() <= 1) {
            return list;
        } else {
            // 选择基准值, 后续循环需跳过此基准值
            int pivot = list.get(0);
            List<Integer> less = new ArrayList<>();
            List<Integer> greater = new ArrayList<>();
            // 必须跳过基准值
            for (int i = 1; i < list.size(); i++) {
                Integer curr = list.get(i);
                if (curr <= pivot) {
                    less.add(curr);
                } else {
                    greater.add(curr);
                }
            }
            List<Integer> integers = quickSort(less);
            integers.add(pivot);
            integers.addAll(quickSort(greater));
            return integers;
        }
    }
}