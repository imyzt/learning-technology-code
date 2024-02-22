package top.imyzt.learning.algorithm.lru;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author imyzt
 * @date 2024/02/22
 * @description 最近最久未使用置换算法
 */
public class LRUCache1 {

    private final Map<String, Object> cache = new LinkedHashMap<>();
    private final int capacity;

    public LRUCache1(int capacity) {
        this.capacity = capacity;
    }

    public void put(String key, Object value) {

        // 不管有没有, 先删除
        cache.remove(key);

        if (cache.size() == capacity) {
            // 队列已满, 删除头部元素
            cache.remove(cache.keySet().iterator().next());
        }

        cache.put(key, value);
    }

    public Object get(String key) {
        Object value = cache.remove(key);
        if (value != null) {
            cache.put(key, value);
        }
        return value;
    }

    @java.lang.Override
    public String toString() {
        return cache.toString();
    }
}
