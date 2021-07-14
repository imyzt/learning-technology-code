package top.imyzt.study.threadlocal.demo.threadlocal;

import lombok.var;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author imyzt
 * @date 2020/02/15
 * @description 描述信息
 */
public class MyThreadLocal<T> {

    private static final AtomicInteger atomic = new AtomicInteger();

    private Integer threadLocalHash = atomic.addAndGet(0x61c88647);

    private static final HashMap<Thread, HashMap<Integer, Object>> threadLocalMap = new HashMap<>();

    private HashMap<Integer, Object> getMap() {
        return threadLocalMap.computeIfAbsent(Thread.currentThread(), curr -> new HashMap<>(16));
    }

    protected T initialValue() {
        return null;
    }

    public void set(T v) {
        var map = getMap();
        map.put(this.threadLocalHash, v);
    }

    @SuppressWarnings("unchecked")
    public T get() {
        var map = getMap();
        if (!map.containsKey(this)) {
            map.put(this.threadLocalHash, initialValue());
        }
        return (T) map.get(this.threadLocalHash);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}