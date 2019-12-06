package com.yzx.weather.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzx.weather.config.AliyunConfig;
import com.yzx.weather.craw.craw_1.weather.WeatherPilpline_use1;
import com.yzx.weather.craw.craw_1.weather.WeatherProcessor_use1;
import com.yzx.weather.craw.craw_2.weather.WeatherPilpline_use2;
import com.yzx.weather.craw.craw_2.weather.WeatherProcessor_use2;
import com.yzx.weather.mapper.City2Mapper;
import com.yzx.weather.mapper.CityMapper;
import com.yzx.weather.pojo.City;
import com.yzx.weather.pojo.City2;
import com.yzx.weather.pojo.UploadResult;
import com.yzx.weather.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.List;

@RestController
public class WeatherController {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private City2Mapper city2Mapper;

    @Autowired
    private AliyunConfig aliyunConfig;

    @GetMapping("/w1")
    public String geW1tDownUrl(@RequestParam String name,
                             @RequestParam(defaultValue = "15",required = false) Integer t){

        List<City> cityList = cityMapper.selectList(new QueryWrapper<City>().in("name", name));
        if(cityList.size()==0){
            return "地区中文名输入错误 请重新检查与 http://lishi.tianqi.com/ 下的名字是否相同";
        }

        City city=cityList.get(0);
        String url="http://lishi.tianqi.com/"+city.getCity()+"/201101.html?tdsourcetag=s_pctim_aiomsg";
        String localPath="/w1/"+city.getName()+".txt";

        Spider.create( new WeatherProcessor_use1() )
                .addUrl(url).thread(t)
                .addPipeline(new WeatherPilpline_use1())
                .setScheduler(new QueueScheduler())							//设置内存队列
                .run();

        UploadResult result = uploadService.upload(localPath);
        if(result.getStatus().equals("error")){
            return "爬取失败！请检查该地区是否有数据 比如澳门没有数据，或者服务器有问题 请联系yzx";
        }else {
            uploadService.initFile(localPath);
            return "爬取成功！ 请访问："+result.getName();
        }

    }

    @GetMapping("/w2")
    public String geW1tDownUr2(@RequestParam String name,
                               @RequestParam(defaultValue = "50",required = false) Integer t){
        List<City2> city2List = city2Mapper.selectList(new QueryWrapper<City2>().in("name", name));
        if(city2List.size()==0){
            return "城市中文名输入错误 请重新检查与 http://www.tianqihoubao.com/lishi/ 下的名字是否相同";
        }

        City2 city2=city2List.get(0);
        String url="http://www.tianqihoubao.com/lishi/"+city2.getCity()+"/month/201101.html";
        String localPath="/w2/"+city2.getName()+".txt";

        Spider.create( new WeatherProcessor_use2() )
                .addUrl(url).thread(t)
                .addPipeline(new WeatherPilpline_use2())
                .setScheduler(new QueueScheduler())							//设置内存队列
                .run();

        UploadResult result = uploadService.upload(localPath);
        if(result.getStatus().equals("error")){
            return "爬取失败！请检查该地区是否有数据，或者服务器有问题 请联系yzx";
        }else {
            uploadService.initFile(localPath);
            return "爬取成功！ 请访问："+result.getName();
        }
    }
}
