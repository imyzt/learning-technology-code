package top.imyzt.learning.dp.flow.custom;

/**
 * @author imyzt
 * @date 2022/12/07
 * @description 观察者对象
 */
@FunctionalInterface
public interface Observer<T> {

    /**
     * 观察者实现接口
     * @param o 数据
     */
    void apply(T o);
}