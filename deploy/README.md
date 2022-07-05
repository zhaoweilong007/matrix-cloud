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

>ps:在gateway网关集成sentinel时，需要添加JVM参数`-Dcsp.sentinel.app.type=1`,将应用识别为网关，否则看不到api管理页面

## rocketmq

## seata

## ELK

## SkyWalking

## Promethues+grafana