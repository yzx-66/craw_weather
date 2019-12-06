package com.yzx.weather.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzx.weather.config.AliyunConfig;
import com.yzx.weather.mapper.CityMapper;
import com.yzx.weather.pojo.City;
import com.yzx.weather.pojo.UploadResult;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;

@Service
public class UploadService {

    @Autowired
    private OSSClient oSSClient;

    @Autowired
    private AliyunConfig aliyunConfig;

    @Autowired
    private CityMapper cityMapper;

    public void initFile(String localPath){
        File f=new File(localPath);
        if(f.exists()){
            f.delete();
        }
    }


    public UploadResult upload(String localPath){
        String aliyunPath=localPath.substring(1);
        File f=new File(localPath);

        UploadResult result=new UploadResult();
        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        try {
            ObjectMetadata objectMetadata=new ObjectMetadata();
            objectMetadata.setContentType("application/octet-stream");
            oSSClient.putObject(aliyunConfig.getBucketName(), aliyunPath, new ByteArrayInputStream(toByteArray(f)),objectMetadata);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus("error");
            return result;
        }

        result.setStatus("done");
        //访问路径
        result.setName(aliyunConfig.getUrlPrefix()+aliyunPath);
        result.setUid(String.valueOf(System.currentTimeMillis()));
        return result;
    }

    public  byte[] toByteArray(File file) throws IOException {
        File f = file;
        if (!f.exists()) {
            throw new FileNotFoundException("file not exists");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }
}
