package cn.edu.jyu.weather.controller;

import cn.edu.jyu.weather.common.Result;
import cn.edu.jyu.weather.entity.City;
import cn.edu.jyu.weather.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    //城市搜索接口
    @GetMapping("/search")
    public Result<List<City>> search(@RequestParam String q) {
        if (q == null || q.trim().isEmpty()) {
            return Result.error(400, "请输入城市名");
        }
        List<City> list = cityService.searchCity(q);
        return Result.success(list);
    }
}