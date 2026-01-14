package cn.edu.jyu.weather.controller;

import cn.edu.jyu.weather.common.Result;
import cn.edu.jyu.weather.service.WeatherAPIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    // Jackson 的工具类，用于把 String 转成 Object
    @Autowired
    private ObjectMapper objectMapper;


    private Object parseJson(String rawData) {
        try {
            if (rawData == null) return null;
            // 将字符串解析为通用对象 (Map/List)
            return objectMapper.readValue(rawData, Object.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 实时天气
    @GetMapping("/now")
    public Result<Object> getNow(@RequestParam(name = "location", defaultValue = "101010100") String location) {
        // 注意：这里变量名也建议改成 location，保持一致
        String rawData = weatherAPIService.getNowWeather(location);
        return Result.success(parseJson(rawData));
    }

    // 3天预报
    @GetMapping("/forecast")
    public Result<Object> getForecast(@RequestParam(name = "location", defaultValue = "101010100") String location) {
        String rawData = weatherAPIService.getForecastWeather(location);
        return Result.success(parseJson(rawData));
    }

    // 24小时逐小时预报
    @GetMapping("/24h")
    public Result<Object> getHourly(@RequestParam(name = "location", defaultValue = "101010100") String location) {
        String rawData = weatherAPIService.getHourlyWeather(location);
        return Result.success(parseJson(rawData));
    }
}