package com.yzx.weather.t_craw1.weather;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzx.weather.mapper.CityMapper;
import com.yzx.weather.pojo.City;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Component
public class WeatherPilpline1 implements Pipeline {

    @Autowired
    private CityMapper cityMapper;

    private static WeatherPilpline1 weatherPilpline;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        weatherPilpline = this;
        weatherPilpline.cityMapper = this.cityMapper;
        // 初使化时将已静态化的testService实例化
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        /**
         * page.putField("日期",date);
         *             page.putField("最高气温",highTem);
         *             page.putField("最低气温",lowTem);
         *             page.putField("天气",weather);
         *             page.putField("风向",cool);
         */
        String city="";
        StringBuffer buffer=new StringBuffer();
        List<Map<String,Object>> data=resultItems.get("data");
        for(Map<String,Object> map:data){
            city= (String) map.get("城市");
            if(StringUtils.isBlank(city)){
                return;
            }
            String date= (String) map.get("日期");
            String highTem= (String) map.get("最高气温");
            String lowTem= (String) map.get("最低气温");
            String weather= (String) map.get("天气");
            String cool= (String) map.get("风向");

            buffer.append("日期：").append(date).append("   最高气温：").append(highTem).append("   最低气温：").append(lowTem).append("   天气：").append(weather).append("   风向：").append(cool).append("\n");
        };
        buffer.append("\n\n");

        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            String in=city.substring(0,1).toUpperCase();
            String dirPath="C:\\Users\\yzx66\\Desktop\\weather\\"+in;

            File dir=new File(dirPath);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String name=weatherPilpline.cityMapper.selectList(new QueryWrapper<City>().in("city",city)).get(0).getName();
            File f=new File(dir+"\\"+name+".txt");
            if(!f.exists()){
                f.createNewFile();
            }

            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (StringIndexOutOfBoundsException s){
            System.out.println("城市："+city+" 爬取该链接失败");
            return;
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(buffer);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
