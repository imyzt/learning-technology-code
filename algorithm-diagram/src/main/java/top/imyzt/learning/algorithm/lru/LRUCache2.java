package top.imyzt.learning.algorithm.lru;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author imyzt
 * @date 2024/02/22
 * @description 最近最久未使用置换算法-利用map特性
 */
public class LRUCache2 {

    private final Map<String, Object> cache = new LinkedHashMap<String, Object>() {
        @java.lang.Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > capacity;
        }
    };
    private final int capacity;

    public LRUCache2(int capacity) {
        this.capacity = capacity;
    }

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    @java.lang.Override
    public String toString() {
        return cache.toString();
    }
}
