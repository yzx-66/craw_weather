package com.yzx.weather.craw.craw_1.city;

import com.yzx.weather.mapper.CityMapper;
import com.yzx.weather.pojo.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class CityPilpline_use1 implements Pipeline {

    @Autowired
    private CityMapper cityMapper;

    private static CityPilpline_use1 cityPilpline;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        cityPilpline = this;
        cityPilpline.cityMapper = this.cityMapper;
        // 初使化时将已静态化的testService实例化
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<City> data= resultItems.get("data");
        for(City c:data){
            cityPilpline.cityMapper.insert(c);
        }
    }
}
