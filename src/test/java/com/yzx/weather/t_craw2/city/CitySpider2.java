package com.yzx.weather.t_craw2.city;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CitySpider2 {

    @Test
    public void testSpider(){
        Spider.create(new CityProcessor2())
                .addUrl("http://www.tianqihoubao.com/lishi/").thread(1)
                .addPipeline(new CityPilpline2())
                //.addPipeline(new ConsolePipeline())									//把已经爬过的链接存入内存
                //.addPipeline(new FilePipeline("C:\\Users\\yzx66\\Desktop"))							//以一般格式存入文件已经爬过的链接
                //.addPipeline(new JsonFilePipeline("C:\\Users\\yzx66\\Desktop"))						//以json格式存入文件已经爬过的链接
                //定制化输出（可以写入数据库、文件或者打印等等）
                .setScheduler(new QueueScheduler())								//设置内存队列
                //.setScheduler(new FileCacheQueueScheduler("E:\\scheduler"))		//设置文件队列
                //.setScheduler(new RedisScheduler("127.0.0.1"))						//设置Redis队列（只能有一个Scheduler)
                .run();
    }
}
