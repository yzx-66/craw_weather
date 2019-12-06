package com.yzx.weather.craw.craw_2.city;

import com.yzx.weather.mapper.City2Mapper;
import com.yzx.weather.pojo.City2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class CityPilpline_use2 implements Pipeline {

    @Autowired
    private City2Mapper cityMapper2;

    private static CityPilpline_use2 cityPilpline;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        cityPilpline = this;
        cityPilpline.cityMapper2 = this.cityMapper2;
        // 初使化时将已静态化的testService实例化
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<City2> data= resultItems.get("data");
        for(City2 city2:data){
            cityPilpline.cityMapper2.insert(city2);
        };
    }
}
