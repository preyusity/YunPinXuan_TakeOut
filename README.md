# YuPinXuan_Take_Out

#### 介绍
御品轩配送点单系统以及系统优化

#### 软件架构
本项目为前后端分离项目，主要完成后端开发。
使用了SpringBoot和Mybatisplus框架，
使用了Redis作为二级缓存，使用主从复制Mysql实现读写分离，以及使用Nginx反向代理。

#### 项目运行
本项目需使用两台服务器，运行时需要部署服务器需开启数据库主从复制，使用Nginx配置
反向代理和负载均衡，需修改yml中Redis和Mysql的ip， 需注意：缓存文件地址改为/usr/local/img/。