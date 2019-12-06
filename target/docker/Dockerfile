# From java image, version : 8
FROM java:8

#挂载app目录
#VOLUME /app

#将maven构建好的jar添加到镜像中
#accout-mange-1.0-SNAPSHOT 是我的target下生成的jar包名，accout-mange-1.0 是我上传到docker的镜像名
ADD target/wether-1.0-SNAPSHOT.jar wether-1.0.jar

#服务的端口
EXPOSE 8080

#accout-mange-1.0是上传到docker的镜像名
ENTRYPOINT ["java", "-jar", "wether-1.0.jar"]
