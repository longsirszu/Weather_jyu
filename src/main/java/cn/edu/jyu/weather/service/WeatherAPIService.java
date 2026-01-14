package cn.edu.jyu.weather.service;

import cn.edu.jyu.weather.entity.ErrorLog;
import cn.edu.jyu.weather.mapper.ErrorLogMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherAPIService {

    @Value("${weather-api.weather-host}")
    private String weatherHost;

    @Value("${weather-api.now-weather-path}")
    private String nowPath;

    @Value("${weather-api.forecast-path}")
    private String forecastPath;

    @Value("${weather-api.eh-weather-path}")
    private String hourlyPath;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    // ✅ 新增：注入日志 Mapper
    @Autowired
    private ErrorLogMapper errorLogMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private String callQWeatherApi(String path, String location) {
        // 1. 生成 Token
        String token = jwtService.generateToken();

        // 2. 构建 Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 3. URL
        String url = String.format("%s%s?location=%s", weatherHost, path, location);

        try {
            // 4. 发起请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();

        } catch (Exception e) {
            // “激活”数据库日志
            try {
                System.out.println(">>> 捕获到异常，正在写入数据库日志...");

                ErrorLog log = new ErrorLog();
                log.setCityName(location); // 记录哪个城市出错了
                log.setRequestTime(LocalDateTime.now());

                // 判断错误类型
                String msg = e.getMessage();
                if (msg != null && msg.contains("404")) {
                    log.setErrorType("定位失败(404)");
                } else if (msg != null && msg.contains("timeout")) {
                    log.setErrorType("响应超时");
                } else {
                    log.setErrorType("系统异常");
                }

                // 截取错误信息（数据库字段限制200字符，防止太长报错）
                if (msg != null && msg.length() > 190) {
                    msg = msg.substring(0, 190) + "...";
                }
                log.setErrorMsg(msg);

                // 执行插入！
                errorLogMapper.insert(log);


            } catch (Exception dbError) {
                // 如果数据库自己都挂了，打印一下就行，别让程序崩了
                System.err.println(dbError.getMessage());
            }

            // 返回给前端的 JSON (保持不变)
            try {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("code", "500");
                errorMap.put("msg", "后端服务异常: " + e.getMessage());
                return objectMapper.writeValueAsString(errorMap);
            } catch (Exception jsonEx) {
                return "{\"code\":\"500\", \"msg\":\"Error\"}";
            }
        }
    }


    @Cacheable(value = "realtime", key = "#location")
    public String getNowWeather(String location) {
        return callQWeatherApi(nowPath, location);
    }

    public String getForecastWeather(String location) {
        return callQWeatherApi(forecastPath, location);
    }

    @Cacheable(value = "hourly", key = "#location")
    public String getHourlyWeather(String location) {
        return callQWeatherApi(hourlyPath, location);
    }
}