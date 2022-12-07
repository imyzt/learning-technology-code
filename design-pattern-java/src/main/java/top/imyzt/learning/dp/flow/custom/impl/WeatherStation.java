package top.imyzt.learning.dp.flow.custom.impl;

/**
 * @author imyzt
 * @date 2022/12/07
 * @description 气象站
 */
public class WeatherStation {

    public static void main(String[] args) {

        // 可观察对象 -> 气象数据发布者
        WeatherDataSubject weatherDataSubject = new WeatherDataSubject();

        // 观察对象 -> 当前气象数据显示器
        CurrentConditionsDisplay currentConditionsDisplay = new CurrentConditionsDisplay();
        // 观察对象 -> 温度显示器
        TemperatureDisplay temperatureDisplay = new TemperatureDisplay();

        // 将观察者注册到可观察者的列表中
        weatherDataSubject.register(currentConditionsDisplay);
        weatherDataSubject.register(temperatureDisplay);

        weatherDataSubject.setMeasurements(14,75);


    }
}