package cn.edu.jyu.weather.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 问候语控制器，提供返回Hello World的GET接口
 */

@RestController // 标记为REST控制器，返回JSON/字符串而非页面
@RequestMapping("/api") // 接口统一前缀,这个是我们后面创建的每一个接口的前缀
public class GreetingController {

    /**
     * GET接口：返回Hello World字符串
     * 访问路径：http://localhost:8080/api/greeting
     */

    @GetMapping("/greeting")
    public String getGreeting() {
        return "Hello World";
    }
}