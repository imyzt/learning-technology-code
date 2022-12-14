package top.imyzt.learning.dp.flow.jdk1;

import java.util.Observable;

/**
 * @author imyzt
 * @date 2022/12/14
 * @description 继承Observable用于标注自己是可被观察的对象
 */
public class ObservableImpl extends Observable {

    private Object data;

    public void changeDataAndNotify(Object data) {
        // 引起观察者注意
        super.setChanged();
        // 设置变化点
        super.notifyObservers(data);
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObservableImpl{" +
                "data=" + data +
                '}';
    }
}