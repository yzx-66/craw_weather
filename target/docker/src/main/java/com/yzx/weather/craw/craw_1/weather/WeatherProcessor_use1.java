package com.yzx.weather.craw.craw_1.weather;

import com.yzx.weather.utils.GetContentUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WeatherProcessor_use1 implements PageProcessor {

    @Autowired
    private RedisTemplate redisTemplate;

    private static WeatherProcessor_use1 weatherProcessor;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        weatherProcessor = this;
        weatherProcessor.redisTemplate = this.redisTemplate;
        //weatherProcessor.redisTemplate.delete("*");
        // 初使化时将已静态化的testService实例化
    }

    @Override
    public void process(Page page) {

        String city=page.getUrl().get().split("/")[3];
        for(int i=11;i<=19;i++){
            for(int j=1;j<=12;j++){
                String month=j<=9 ? "0"+j : ""+j;
                String path="http://lishi.tianqi.com/"+city+"/20"+i+month+".html?tdsourcetag=s_pctim_aiomsg";
                //if(setlock(path,path)){
                    page.addTargetRequest(path);
                //}
            }
        }

        boolean isNewType=false;
        int divNum;
        if(StringUtils.isNoneBlank(page.getHtml().xpath("/html/body/div[7]/div[1]/div[5]/div/div[2]/ul[2]/li[1]/div[1]/a").get()) || StringUtils.isNoneBlank(page.getHtml().xpath("/html/body/div[8]/div[1]/div[5]/div/div[2]/ul[2]/li[1]/div[1]/a").get())){
            isNewType=true;
            divNum=StringUtils.isNoneBlank(page.getHtml().xpath("/html/body/div[7]/div[1]/div[5]/div/div[2]/ul[2]/li[1]/div[1]/a").get()) ? 7 : 8;

        }else {
            divNum=StringUtils.isNoneBlank(page.getHtml().xpath("/html/body/div[7]/div[1]/div[5]/div/div[2]/ul[2]/li[1]/div[1]").get()) ? 7 : 8;
        }

        List<Map<String,Object>> res=new ArrayList<>();

        for(int i=0;i<=31;i++){
            String date=isNewType ? page.getHtml().xpath("/html/body/div["+divNum+"]/div[1]/div[5]/div/div[2]/ul[2]/li["+i+"]/div[1]/a").get()
                    :page.getHtml().xpath("/html/body/div["+divNum+"]/div[1]/div[5]/div/div[2]/ul[2]/li["+i+"]/div[1]").get();
            String highTem=page.getHtml().xpath("/html/body/div["+divNum+"]/div[1]/div[5]/div/div[2]/ul[2]/li["+i+"]/div[2]").get();
            String lowTem=page.getHtml().xpath("/html/body/div["+divNum+"]/div[1]/div[5]/div/div[2]/ul[2]/li["+i+"]/div[3]").get();
            String weather=page.getHtml().xpath("/html/body/div["+divNum+"]/div[1]/div[5]/div/div[2]/ul[2]/li["+i+"]/div[4]").get();
            String cool=page.getHtml().xpath("/html/body/div["+divNum+"]/div[1]/div[5]/div/div[2]/ul[2]/li["+i+"]/div[5]").get();

            if(StringUtils.isNoneBlank(date) && StringUtils.isNoneBlank(highTem) && StringUtils.isNoneBlank(lowTem) && StringUtils.isNoneBlank(weather) && StringUtils.isNoneBlank(cool)){
                Map<String,Object> map=new HashMap<>();
                map.put("城市",city);
                map.put("日期", GetContentUtils.getLabelContent(date));
                map.put("最高气温", GetContentUtils.getLabelContent(highTem));
                map.put("最低气温", GetContentUtils.getLabelContent(lowTem));
                map.put("天气", GetContentUtils.getLabelContent(weather));
                map.put("风向", GetContentUtils.getLabelContent(cool));

                res.add(map);
            }

        }

       page.putField("data",res);
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3000).setSleepTime(100);
    }

    public Boolean setlock(String key,String value) {
        //读取脚本文件
        DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
        lockScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("add.lua")));
        lockScript.setResultType(Boolean.class);

        // 封装参数
        List<Object> keyList = new ArrayList<Object>();
        keyList.add(key);
        keyList.add(value);

        //运行脚本文件 并返回结果
        Boolean result = (Boolean) weatherProcessor.redisTemplate.execute(lockScript, keyList);
        return result;
    }

}
