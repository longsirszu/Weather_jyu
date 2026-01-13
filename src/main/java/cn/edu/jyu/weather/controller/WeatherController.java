package cn.edu.jyu.weather.controller;

import cn.edu.jyu.weather.service.WeatherAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherAPIService weatherAPIService;

    //接口1：查询实时天气
    @GetMapping("/now")
    public String getNow(@RequestParam(defaultValue = "101010100") String city) {
        return weatherAPIService.getNowWeather(city);
    }


    //接口2：查询未来3天预报
    @GetMapping("/forecast")
    public String getForecast(@RequestParam(defaultValue = "101010100") String city) {
        return weatherAPIService.getForecastWeather(city);
    }
}