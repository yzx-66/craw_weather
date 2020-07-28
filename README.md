# 爬虫爬取天气应用

#### 介绍
* 爬取http://lishi.tianqi.com/ 3000多个地区近九年每天的数据 大约1kw条 并部署到云服务器 提供手动选择地区爬取接口
* 爬取http://www.tianqihoubao.com/lishi/ 每个300多个城市进九年数据 并部署到云服务器 提供手动选择地区爬取接口

#### 运行
* 运行数据库脚本 修改配置文件中的数据库用户名和密码
* 修改redis地址（webmagic缓存爬取过的地址）
![输入图片说明](https://images.gitee.com/uploads/images/2020/0728/231751_3d1f2def_5494607.png "屏幕截图.png")

#### 爬取
* 如果要爬取到本地：运行test下的 WeaterSpider1（http://lishi.tianqi.com/） 和 WeaterSpider2（http://www.tianqihoubao.com/lishi/）
* 如果要对外提供爬取接口：修改oos的配置
    ![输入图片说明](https://images.gitee.com/uploads/images/2020/0728/231819_d75a62fa_5494607.png "屏幕截图.png")

#### 使用技术
* WebMagic + SpringBoot + MybatisPlus + Redis + OSS