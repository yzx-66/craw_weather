package com.yzx.weather.t_craw1.weather;

import com.yzx.weather.mapper.CityMapper;
import com.yzx.weather.pojo.City;
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
public class WeaterSpider1 {

    @Autowired
    private CityMapper cityMapper;

    private static WeaterSpider1 weaterSpider;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        weaterSpider = this;
        weaterSpider.cityMapper = this.cityMapper;
        // 初使化时将已静态化的testService实例化
    }

    @Test
    public void testSpider(){
        List<City> cityList = weaterSpider.cityMapper.selectList(null);

        String[] urls = new String[cityList.size()];
        int len=0;
        for(City city:cityList) {
            String url="http://lishi.tianqi.com/"+city.getCity()+"/201101.html?tdsourcetag=s_pctim_aiomsg";
            urls[len++]=url;
        };

        Spider.create( new WeatherProcessor1() )
                .addUrl(urls).thread(100)
                .addPipeline(new WeatherPilpline1())
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
