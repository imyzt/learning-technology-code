package top.imyzt.learning.algorithm.chapter02;

import top.imyzt.learning.algorithm.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2021/04/24
 * @description 选择排序
 */
public class SelectionSort {

    public static void main(String[] args) {
        List<Integer> list = ListUtils.randomNotRepeatList(10, 20);
        System.out.println("原数组: " + list);

        ArrayList<Integer> newList = new ArrayList<>(list.size());
        for (int i = list.size() - 1; i >= 0; i--) {
            int smallestIdx = findSmallestIdx(list);
            newList.add(list.remove(smallestIdx));
        }

        System.out.println("选择排序后数组: " + newList);
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