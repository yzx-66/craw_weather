package com.yzx.weather.t_craw2.city;

import com.yzx.weather.pojo.City;
import com.yzx.weather.pojo.City2;
import com.yzx.weather.utils.GetContentUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class CityProcessor2 implements PageProcessor {

    @Override
    public void process(Page page) {
        List<City2> data=new ArrayList<>();
        for(int i=1;i<=34;i++){
            for(int j=1;j<=25;j++){
                String pro=page.getHtml().xpath("//*[@id=\"content\"]/div[3]/dl["+i+"]/dt/a/b/text()").get();
                String name=page.getHtml().xpath("//*[@id=\"content\"]/div[3]/dl["+i+"]/dd/a["+j+"]/text()").get();
                if(StringUtils.isBlank(name)){
                    continue;
                }
                String content=page.getHtml().xpath("//*[@id=\"content\"]/div[3]/dl["+i+"]/dd/a["+j+"]/@href").get();
                String city=content.split("/")[2].split(".html")[0];
                City2 c=City2.builder().city(city).name(name).province(pro).build();
                data.add(c);
            }
        }
        page.putField("data",data);
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3000).setSleepTime(100).setCharset("gb2312");
    }
}
