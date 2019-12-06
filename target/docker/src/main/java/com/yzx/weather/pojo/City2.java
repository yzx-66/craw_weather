package com.yzx.weather.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("city_2")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City2 {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String city;

    private String name;

    private String province;
}
