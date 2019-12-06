package com.yzx.weather.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("city")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String initials;

    private String city;

    private String name;
}
