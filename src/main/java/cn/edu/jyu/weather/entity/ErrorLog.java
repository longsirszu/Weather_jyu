package cn.edu.jyu.weather.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_error_log")
public class ErrorLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("city_name")
    private String cityName; // 查询时用的城市名或ID

    @TableField("error_type")
    private String errorType; // 错误类型：超时/400/500等

    @TableField("error_msg")
    private String errorMsg;  // 具体的报错信息

    @TableField("request_time")
    private LocalDateTime requestTime; // 发生时间
}