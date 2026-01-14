package cn.edu.jyu.weather.mapper;

import cn.edu.jyu.weather.entity.ErrorLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErrorLogMapper extends BaseMapper<ErrorLog> {
}