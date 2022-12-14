package top.imyzt.learning.dp.flow.jdk1;


import java.util.Observable;
import java.util.Observer;

/**
 * @author imyzt
 * @date 2022/12/14
 * @description 观察着对象
 */
public class ObserverImpl implements Observer {

    private Object lastData;
    private final String name;

    public ObserverImpl(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("观察者[" + name + "]检测到观察对象发生了改变, lastData=" + lastData + ",newData=" + (lastData = arg));
    }
}