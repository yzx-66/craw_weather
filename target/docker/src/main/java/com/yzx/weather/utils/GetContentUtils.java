package com.yzx.weather.utils;

public class GetContentUtils {

    public static String getLabelContent(String content){
        String[] split_1 = content.split(">");
        String[] spilt_2 = split_1[1].split("<");
        return spilt_2[0].replaceAll("\n","");
    }

    public static String getBackslashContent(String content){
        return content.split("/")[1];
    }
}
