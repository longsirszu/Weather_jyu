package cn.edu.jyu.weather.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@TableName("t_city") // 显式指定表名
@Data
public class City {
    // 主键显式指定字段名，location_id非自增，设置type=INPUT
    @TableId(value = "location_id", type = IdType.INPUT)
    private Integer locationId;

    // 以下字段显式指定数据库列名，避免驼峰转换异常（可选，但更稳妥）
    @TableField("location_name_en")
    private String locationNameEn;

    @TableField("location_name_zh")
    private String locationNameZh;

    @TableField("iso_3166_1")
    private String iso31661;

    @TableField("country_region_zh")
    private String countryRegionZh;

    @TableField("adm1_name_zh")
    private String adm1NameZh;

    @TableField("adm2_name_zh")
    private String adm2NameZh;

    @TableField("latitude")
    private BigDecimal latitude;

    @TableField("longitude")
    private BigDecimal longitude;

    @TableField("ad_code")
    private String adCode;

    @TableField("timezone")
    private String timezone;
}