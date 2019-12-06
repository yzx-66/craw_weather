package com.yzx.weather.t_craw2.weather;

import com.yzx.weather.mapper.City2Mapper;
import com.yzx.weather.mapper.CityMapper;
import com.yzx.weather.pojo.City;
import com.yzx.weather.pojo.City2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WeaterSpider2 {

    @Autowired
    private City2Mapper city2Mapper;

    private static WeaterSpider2 weaterSpider;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        weaterSpider = this;
        weaterSpider.city2Mapper = this.city2Mapper;
        // 初使化时将已静态化的testService实例化
    }

    @Test
    public void testSpider(){
        List<City2> cityList = weaterSpider.city2Mapper.selectList(null);

        String[] urls = new String[cityList.size()];
        int len=0;
        for(City2 city:cityList) {
            String url="http://www.tianqihoubao.com/lishi/"+city.getCity()+"/month/201101.html";
            urls[len++]=url;
        };

        Spider.create( new WeatherProcessor2() )
                .addUrl(urls).thread(50)
                .addPipeline(new WeatherPilpline2())
                //.addPipeline(new ConsolePipeline())									//把已经爬过的链接存入内存
                //.addPipeline(new FilePipeline("C:\\Users\\yzx66\\Desktop"))							//以一般格式存入文件已经爬过的链接
                //.addPipeline(new JsonFilePipeline("C:\\Users\\yzx66\\Desktop"))						//以json格式存入文件已经爬过的链接
                //定制化输出（可以写入数据库、文件或者打印等等）
                //.setScheduler(new QueueScheduler())							//设置内存队列
                //.setScheduler(new FileCacheQueueScheduler("E:\\scheduler"))		//设置文件队列
                .setScheduler(new RedisScheduler("192.168.56.129"))						//设置Redis队列（只能有一个Scheduler)
                .run();
    }
}
