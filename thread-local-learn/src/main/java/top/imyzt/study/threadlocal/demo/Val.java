package top.imyzt.study.threadlocal.demo;

import java.util.Objects;

/**
 * @author imyzt
 * @date 2020/02/15
 * @description 引用类型
 */
public class Val<V> {

    private V v;

    public V get() {
        return v;
    }

    public void set(V v) {
        this.v = v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Val<?> val = (Val<?>) o;
        return Objects.equals(v, val.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v);
    }
}