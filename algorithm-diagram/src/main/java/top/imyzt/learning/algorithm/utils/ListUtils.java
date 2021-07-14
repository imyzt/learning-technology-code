package top.imyzt.learning.algorithm.utils;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author imyzt
 * @date 2021/04/24
 * @description List工具类
 */
public class ListUtils {

    private static final Random RANDOM = new Random();

    public static List<Integer> randomList(int len, int bound) {
        ArrayList<Integer> ts = new ArrayList<>(len);

        IntStream.range(0, len).map(d -> RANDOM.nextInt(bound)).forEach(ts::add);
        return ts;
    }

    public static List<Integer> randomNotRepeatList(int len, int bound) {
        Set<Integer> ts = new HashSet<>(len);
        while (ts.size() < len) {
            ts.add(RANDOM.nextInt(bound));
        }
        return new ArrayList<>(ts);
    }
}