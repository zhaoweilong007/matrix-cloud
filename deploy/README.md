# 部署说明文档

## nacos

- 版本2.1.0
- ui地址：localhost:8848
- 默认账号nacos/nacos

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

- 版本：1.8.4
- ui地址：localhost:8088
- 默认账号密码sentinel/sentinel

部署sentinel服务

```shell
java -Dserver.port=8088 -Dcsp.sentinel.dashboard.server=localhost:8088 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
```

> ps:在gateway网关集成sentinel时，需要添加JVM参数`-Dcsp.sentinel.app.type=1`,将应用识别为网关，否则看不到api管理页面

## seata

### 下载seata1.5.2[seata下载](https://github.com/seata/seata/releases)

- ui地址：localhost:7091
- 默认账号：seata/seata

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

## elasticSearch

- 版本：7.17.5

下载elasticSearch压缩包，修改config下elasticSearch.yml配置，启动elasticSearch

如需设置密码
elasticSearch.yml 增加以下配置：

```yaml
http.cors.enabled: true
http.cors.allow-origin: "*"
http.cors.allow-headers: Authorization
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: true
```

bin目录下运行

```shell
elasticsearch-setup-passwords.bat interactive
```

## SkyWalking

- APM版本：9.1.0
- ui地址：localhost:8888
- java agent版本：8.11.0

## apm部署

下载skywalking-apm

修改apm的config下application.yml配置

cluster使用naocs、storage使用elasticsearch

修改wenapp下webapp.yml，端口修改为8888

启动bin目录下startup.bat脚本

## agent配置

注意gateway应用的agent和普通应用servlet的agent要分开

gateway应用的agent需要添加以下两个插件：

- apm-spring-cloud-gateway对应版本的插件

- apm-spring-webflux对应版本的插件

插件在optional-plugins目录下，复制到plugins目录下即可

可额外增加插件，将optional-plugins下插件拷贝到plugins目录下

应用使用agent代理：

- JAR包 使用命令行启动应用时，添加-javaagent参数。比如：

```shell
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar -jar yourApp.jar
```

需要在idea的坏境变量增加以下参数：

- SW_AGENT_NAME=yourAppName

SW_AGENT_NAME修改为自己的应用名称，默认的后端地址为127.0.0.1:11800
也可以修改后端地址：

- SW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800

对地址进行忽略：

添加apm-trace-ignore-plugin插件

在config目录下 增加apm-trace-ignore-plugin.config配置文件

如果需要开启sql参数显示，修改agent.config将参数plugin.jdbc.trace_sql_parameters设置为true

## rocketmq

## ELK

## Promethues+grafana

## other

- gateway api endpoints:
  /actuator/gateway/routes/{id},methods=[DELETE]，删除单个路由
  /actuator/gateway/routes/{id},methods=[POST]，增加单个路由
  /actuator/gateway/routes/{id},methods=[GET]，查看单个路由
  /actuator/gateway/routes],methods=[GET]，获取路由列表
  /actuator/gateway/refresh,methods=[POST]，路由刷新
  /actuator/gateway/globalfilters,methods=[GET]，获取全局过滤器列表
  /actuator/gateway/routefilters,methods=[GET]，路由过滤器工厂列表
  /actuator/gateway/routes/{id}/combinedfilters,methods=[GET]，获取单个路由的联合过滤器