package cn.edu.jyu.weather.entity;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

/**
 * 天气数据结果封装（支持部分数据保留）
 */
@Data
@Builder
public class WeatherResult {
    // 提示信息（success/响应超时/定位失败等）
    private String msg;
    // 实时天气数据（可能为null）
    private Map<String, Object> realtimeData;
    // 24小时逐小时天气数据（可能为null）
    private Map<String, Object> hourlyData;
}