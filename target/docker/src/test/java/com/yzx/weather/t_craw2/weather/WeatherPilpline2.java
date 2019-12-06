package com.yzx.weather.t_craw2.weather;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzx.weather.mapper.City2Mapper;
import com.yzx.weather.mapper.CityMapper;
import com.yzx.weather.pojo.City;
import com.yzx.weather.pojo.City2;
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
public class WeatherPilpline2 implements Pipeline {

    @Autowired
    private City2Mapper city2Mapper;

    private static WeatherPilpline2 weatherPilpline;
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        weatherPilpline = this;
        weatherPilpline.city2Mapper = this.city2Mapper;
        // 初使化时将已静态化的testService实例化
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        /**
         *  map.put("城市",city);
         *                 map.put("日期", date);
         *                 map.put("天气", weather);
         *                 map.put("气温",temp);
         *                 map.put("风向", cool);
         */
        String city=null,name=null,pro=null;
        StringBuffer buffer=new StringBuffer();
        List<Map<String,Object>> data=resultItems.get("data");

        for(Map<String,Object> map:data){
            city= (String) map.get("城市");
            if(StringUtils.isBlank(city)){
                return;
            }
            if(StringUtils.isBlank(name)){
                City2 city2 = weatherPilpline.city2Mapper.selectList(new QueryWrapper<City2>().in("city", city)).get(0);
                name= city2.getName();
                pro=city2.getProvince();
            }
            String date= (String) map.get("日期");
            String weather= (String) map.get("天气");
            String temp= (String) map.get("气温");
            String cool= (String) map.get("风向");

            buffer.append("省份：").append(pro)
                    .append("     城市：").append(name)
                    .append("     日期：").append(date)
                    .append("     天气：").append(weather)
                    .append("     气温：").append(temp)
                    .append("     风向：").append(cool).append("\n");
        };
        buffer.append("\n\n");

        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            String dirPath="C:\\Users\\yzx66\\Desktop\\weather\\"+pro;

            File dir=new File(dirPath);
            if(!dir.exists()){
                dir.mkdirs();
            }

            File f=new File(dirPath+"\\"+name+".txt");
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
