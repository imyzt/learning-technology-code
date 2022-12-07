package top.imyzt.learning.dp.flow.custom.impl;

import top.imyzt.learning.dp.flow.custom.Observer;
import top.imyzt.learning.dp.flow.custom.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author imyzt
 * @date 2022/12/07
 * @description 气象站被观察者
 */
public class WeatherDataSubject implements Subject<WeatherData> {

    private final List<Observer<WeatherData>> observers = new ArrayList<>();
    private WeatherData weatherData;

    @Override
    public void register(Observer<WeatherData> o) {
        observers.add(o);
    }

    @Override
    public void remove(Observer<WeatherData> o) {
        observers.removeIf(observer -> Objects.equals(o, observer));
    }

    @Override
    public void notifyObservers() {
        for (Observer<WeatherData> observer : observers) {
            observer.apply(weatherData);
        }
    }

    public void measurementsChanged() {
        notifyObservers();
    }

    public void setMeasurements(Integer temperature, Integer humidity) {
        this.weatherData = new WeatherData(temperature, humidity);
        // 通知所有观察者
        measurementsChanged();
    }
}

class WeatherData {
    private final Integer temperature;
    private final Integer humidity;

    public Integer getTemperature() {
        return temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public WeatherData(Integer temperature, Integer humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }
}