package cn.edu.jyu.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherAPIService {

    @Value("${weather-api.weather-host}")
    private String weatherHost;

    @Value("${weather-api.now-weather-path}")
    private String nowPath;

    @Value("${weather-api.forecast-path}")
    private String forecastPath; // 读取未来天气的路径

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    /**
     * 通用的 HTTP 请求发送方法
     * @param path 接口路径 (如 /v7/weather/now)
     * @param location 城市ID
     */
    private String callQWeatherApi(String path, String location) {
        // 每次请求时，自动生成最新的 Token
        String token = jwtService.generateToken();

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 拼接完整 URL
        String url = String.format("%s%s?location=%s", weatherHost, path, location);

        // 发送请求
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            return "{\"code\":\"500\", \"msg\":\"调用失败: " + e.getMessage() + "\"}";
        }
    }


    //获取实时天气
    public String getNowWeather(String location) {
        return callQWeatherApi(nowPath, location);
    }

    //获取未来3天预报
    public String getForecastWeather(String location) {
        return callQWeatherApi(forecastPath, location);
    }
}