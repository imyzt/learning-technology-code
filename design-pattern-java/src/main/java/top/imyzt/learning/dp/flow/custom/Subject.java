package top.imyzt.learning.dp.flow.custom;

/**
 * @author imyzt
 * @date 2022/12/07
 * @description 被观察对象
 */
public interface Subject<T> {

    /**
     * 注册
     * @param o 观察者
     */
    void register(Observer<T> o);

    /**
     * 取消注册
     * @param o 观察者
     */
    void remove(Observer<T> o);

    /**
     * 通知所有观察者
     */
    void notifyObservers();
}