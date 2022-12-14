package top.imyzt.learning.dp.flow.jdk1;

/**
 * @author imyzt
 * @date 2022/12/14
 * @description jdk观察者对象实验
 */
public class ObserverApi {

    public static void main(String[] args) {

        // 被观察对象
        ObservableImpl observable = new ObservableImpl();

        // 观察者
        ObserverImpl observerA = new ObserverImpl("A");
        ObserverImpl observerB = new ObserverImpl("B");

        // 将监听者注册到被观察者中
        observable.addObserver(observerA);
        observable.addObserver(observerB);

        for (int i = 0; i < 10; i++) {
            observable.changeDataAndNotify(i);
        }
    }
}