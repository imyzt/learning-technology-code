package top.imyzt.learning.algorithm.chapter01;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author imyzt
 * @date 2021/04/19
 * @description 二分查找
 */
public class BinarySearch {

    public static void main(String[] args) {

        Integer[] arr = {10, 21, 23, 28, 231, 290, 331};
        int target = 100;

        List<Integer> list = Arrays.asList(arr);
        Collections.sort(list);
        Integer index = binarySearch(list, target);
        System.out.println(index);
    }

    private static Integer binarySearch(List<Integer> list, int target) {

        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {

            int mid = (low + high) / 2;
            Integer num = list.get(mid);
            if (target == num) {
                return mid;
            } else if (target < num) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return null;
    }

}