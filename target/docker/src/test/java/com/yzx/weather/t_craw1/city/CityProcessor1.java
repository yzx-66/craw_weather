package com.yzx.weather.t_craw1.city;

import com.yzx.weather.pojo.City;
import com.yzx.weather.utils.GetContentUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class CityProcessor1 implements PageProcessor {

    @Override
    public void process(Page page) {
        List<City> data=new ArrayList<>();
        for(int i=0;i<26;i++){
            char in= (char) ('A'+i);
            if(in=='I' || in=='U' || in=='V' || i=='O'){
                continue;
            }
            String initials=page.getHtml().xpath("//*[@id=\"city-"+in+"\"]/th/a/@title").get().split("/")[0];

            for(int j=1;j<320;j++){
                String city="",name="";
                String content=page.getHtml().xpath("//*[@id=\"city-"+initials+"\"]/td/ul/li["+j+"]/a/@href").get();
                if(StringUtils.isNoneBlank(content)){
                    city= content.split("/")[0];
                    name=GetContentUtils.getLabelContent(page.getHtml().xpath("//*[@id=\"city-"+initials+"\"]/td/ul/li["+j+"]/a").get());
                }else {
                    continue;
                }
                City c=City.builder().city(city).name(name).initials(initials).build();
                data.add(c);
            }
        }
        page.putField("data",data);
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3000).setSleepTime(100);
    }
}
