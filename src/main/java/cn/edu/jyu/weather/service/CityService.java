package cn.edu.jyu.weather.service;

import cn.edu.jyu.weather.entity.City;
import cn.edu.jyu.weather.mapper.CityMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    /**
     * 根据关键词搜索城市
     * @param keyword 用户输入的词 (如 "北京")
     * @return 城市列表
     */
    public List<City> searchCity(String keyword) {
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();

        // 逻辑：中文名 包含 keyword
        // like 是模糊查询，limit 10 是限制返回条数，防止一下把几千条都查出来
        queryWrapper.like("location_name_zh", keyword)
                .last("LIMIT 10");

        return cityMapper.selectList(queryWrapper);
    }
}