package top.imyzt.learning.algorithm.lru;


import cn.hutool.core.util.RandomUtil;

/**
 * @author imyzt
 * @date 2024/02/22
 * @description LRU Test Method
 */
public class LRUTest {

    public static void main(String[] args) {
        LRUCache1 lruCache1 = new LRUCache1(5);
        LRUCache2 lruCache2 = new LRUCache2(5);

        for (int i = 0; i < 1000; i++) {
            String key = RandomUtil.randomInt(30) + "";
            System.out.println(key + "---");
            lruCache1.put(key, i);
            lruCache2.put(key, i);
        }

        System.out.println(lruCache1);
        System.out.println(lruCache2);
    }
}
