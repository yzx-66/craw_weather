package com.yzx.weather.craw.craw_2.weather;

import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherProcessor_use2 implements PageProcessor {

    @Override
    public void process(Page page) {

        String city=page.getUrl().get().split("/")[4];
        for(int i=11;i<=19;i++){
            for(int j=1;j<=12;j++){
                String month=j<=9 ? "0"+j : ""+j;
                String path="http://www.tianqihoubao.com/lishi/"+city+"/month/20"+i+month+".html";
                page.addTargetRequest(path);
            }
        }

        List<Map<String,Object>> res=new ArrayList<>();
        //日期	天气状况	气温	风力风向
        for(int i=2;i<=32;i++){
            String date=page.getHtml().xpath("//*[@id=\"content\"]/table/tbody/tr["+i+"]/td[1]/a/text()").get();
            String weather=page.getHtml().xpath("//*[@id=\"content\"]/table/tbody/tr["+i+"]/td[2]/text()").get();
            String temp=page.getHtml().xpath("//*[@id=\"content\"]/table/tbody/tr["+i+"]/td[3]/text()").get();
            String cool=page.getHtml().xpath("//*[@id=\"content\"]/table/tbody/tr["+i+"]/td[4]/text()").get();

            if(StringUtils.isNoneBlank(date) && StringUtils.isNoneBlank(weather) && StringUtils.isNoneBlank(temp) && StringUtils.isNoneBlank(cool)){
                Map<String,Object> map=new HashMap<>();
                map.put("城市",city);
                map.put("日期", date.replaceAll("\n","").replaceAll(" ",""));
                map.put("天气", weather.replaceAll("\n","").replaceAll(" ",""));
                map.put("气温",temp.replaceAll("\n","").replaceAll(" ",""));
                map.put("风向", cool.replaceAll("\n","").replaceAll(" ",""));

                res.add(map);
            }
        }
       page.putField("data",res);
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3000).setSleepTime(100).setTimeOut(100000);
    }

}
