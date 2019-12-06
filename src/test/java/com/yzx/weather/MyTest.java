package com.yzx.weather;

import org.junit.Test;

import java.io.*;

public class MyTest {

    @Test
    public void test() throws IOException {
        traverseFolder("C:\\Users\\yzx66\\Desktop\\weather");
    }

    public void traverseFolder(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder(file2.getAbsolutePath());
                    } else {
                        System.out.println("文件path:" + file2.getAbsolutePath()+"\t 文件名："+file2.getName());
                        writeAppenFile(file2);
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    public void writeAppenFile(File file) throws IOException {
        StringBuffer buffer=new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
        String s = null;

        while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            buffer.append(s).append("\n");
        }
        br.close();
        buffer.append("\n\n\n");

        File w=new File("C:\\Users\\yzx66\\Desktop\\w.txt");
        FileWriter fw = new FileWriter(w, true);
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

    @Test
    public void test2(){
        String content="http://www.tianqihoubao.com/lishi/beijing/month/201101.html";
        System.out.println(content.split("/"));
    }
}
