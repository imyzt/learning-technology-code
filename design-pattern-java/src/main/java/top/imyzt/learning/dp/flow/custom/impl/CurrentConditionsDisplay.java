package top.imyzt.learning.dp.flow.custom.impl;

import top.imyzt.learning.dp.flow.custom.DisplayElement;
import top.imyzt.learning.dp.flow.custom.Observer;

/**
 * @author imyzt
 * @date 2022/12/07
 * @description 气象站显示器 -> 当前状况显示器
 */
public class CurrentConditionsDisplay implements Observer<WeatherData>, DisplayElement {

    private WeatherData weatherData;

    @Override
    public void display() {
        System.out.printf("[%s]Current temperature = %s°, Current humidity = %s%%%n",
                this.getClass().getSimpleName(), weatherData.getTemperature(), weatherData.getHumidity());
    }

    @Override
    public void apply(WeatherData o) {
        this.weatherData = o;
        this.display();
    }
}