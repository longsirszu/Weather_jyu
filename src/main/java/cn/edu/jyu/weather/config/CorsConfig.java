package cn.edu.jyu.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局CORS配置，解决前后端跨域问题
 */
@Configuration // 标记为配置类
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许前端5173端口访问（Vue默认端口）
        config.addAllowedOrigin("http://localhost:5173");
        // 允许所有请求方法（GET/POST等）
        config.addAllowedMethod("*");
        // 允许所有请求头
        config.addAllowedHeader("*");
        // 允许携带Cookie（可选，实训用不到但建议加上）
        config.setAllowCredentials(true);

        // 配置生效的接口路径（所有接口）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}