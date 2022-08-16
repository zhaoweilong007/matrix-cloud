# 部署说明文档

## nacos

单机部署使用外部mysql

mysql初始化`nacos_config`数据库，导入`nacos下nacos-mysql.sql`初始化数据库

修改nacos下`nacos-standlone-mysql.env`环境配置，其中mysql相关配置改成自己对应的

启动nacos服务

```shell
docker-compose -f nacos/docker-compose.yml up -d
```

或者下载nacos安装包，解压，进入解压目录，以单机部署

修改conf文件夹下nacos配置文件，指定端口，指定数据库配置

```shell
startup.cmd -m standalone
```

## sentinel

部署sentinel服务

```shell
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
```

> ps:在gateway网关集成sentinel时，需要添加JVM参数`-Dcsp.sentinel.app.type=1`,将应用识别为网关，否则看不到api管理页面

## seata

### 下载seata1.5.2[seata下载](https://github.com/seata/seata/releases)

### 服务端配置

解压后修改conf下`application.yml`配置文件，使用nacos作为配置注册中心，

参考seata文件夹下application.yml配置[application.yml](./seata/application.yml)

在nacos新增data-id为seataServer.properties，group为SEATA_GROUP的配置文件，data-id和application.yml的配置保持一致

参考seata文件夹下seataServer.properties配置[seataServer.properties](./seata/seataServer.properties)

进入bin目录启动seata服务

配置中心用的nacos，所以seata-server启动会从nacos读取seataServer.properties的配置，如果配置不正确，会报错

使用db模式，需要先初始化数据库，创建seata数据库，在下载的seata文件夹下script\server\db中运行指定的数据库脚本

这里默认使用AT模式

### 客户端配置

seata分组默认不配置，默认以：spring.application.name值+"-seata-service-group"拼接后的字符串作为分组名

会从nacos读取分组名对应的seata-server的集群名称，seata-server的集群名称在application.yml中seata.register.nacos.cluster进行配置，默认为default

然后会通过集群名去找到TC的真实IP地址

比如system-server服务

默认seata分组名为：service.vgroupMapping.system-server-seata-service-group

需要手动在nacos增加dataid为system-server-seata-service-group，group为SEATA_GROUP的配置文件，类型为text，值对应的就是seata-server的集群名称default

## rocketmq

## ELK

## SkyWalking

## Promethues+grafana