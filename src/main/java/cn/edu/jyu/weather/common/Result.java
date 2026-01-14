package cn.edu.jyu.weather.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code; // 200成功，其他失败
    private String msg;   // 错误信息
    private T data;       // 数据

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.msg = "success";
        r.data = data;
        return r;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> r = new Result<>();
        r.code = code;
        r.msg = msg;
        return r;
    }
}