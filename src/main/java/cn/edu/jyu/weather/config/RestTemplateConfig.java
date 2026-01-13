package cn.edu.jyu.weather.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 1. 创建一个默认的 HttpClient
        // 这个默认客户端自带了 "自动解压 GZIP" 的功能
        HttpClient httpClient = HttpClients.createDefault();

        // 2. 将其包装进 Spring 的工厂
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // 3. 创建 RestTemplate
        return new RestTemplate(factory);
    }
}