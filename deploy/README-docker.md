# docker部署说明

本项目提供docker一键部署各中间件，deploy目录包含以下三个配置

- docker-compose.yml 各中间件部署配置
- docker-prometheus.yml prometheus监控
- docker-matrix.yml 子应用部署配置

以上三个配置公用同一网络，这样的好处是开发时直接使用hostname访问，不需要频繁修改本地IP

下面分别具体说明各配置

- docker compose的环境变量在[.env](.env)中
- 各容器的环境变量可在[env](env)文件夹下查看

# docker-compose.yml说明

包含：

- mysql
- redis
- nacos
- sentinel
- seata-server
- elasticsearch
- logstash
- kibana
- skywalking-oap
- skywalking-ui

> 具体版本查看deploy下[.env文件](./.env),可自行更改，注意每个版本的配置或许有所不同

## mysql

mysql使用dockerFile构建镜像，自动构建镜像`example/mysql`会自动初始化sql文件以下sql

- [matrix.sql](./sql/matrix.sql)
- [nacos.sql](./sql/nacos.sql)
- [seata.sql](./sql/seata.sql)

默认端口3306

默认root密码：matrix

默认创建用户：nacos/nacos

## redis

配置文件映射地址：[redis.conf](./redis/conf/redis.conf)

默认端口：3306

默认密码：matrix


> 如果提示无权限，需要给redis下data文件授予777权限

## sentinel

由于没有官方镜像，使用的是第三方的镜像，具体查看<https://github.com/lk0423/sentinel-dashboard>,自动集成了nacos

## seata-server

seata-server修改配置文件的方式参考了官方的说明，容器启动后，把seata-server/resources拷贝到了seata文件夹下

seata-server读取配置方式为nacos，需要在nacos新建data-id为`seataServer.properties`,group为`SEATA_GROUP`的配置

配置参数具体参考官网文档

配置可自行修改，注意在注册nacos时由于使用的docker部署，导致ip使用的是docker容器ip，这里直接指定环境变量`SEATA_IP`

默认用户密码：seata/seata

## elk

elk部署参考<https://github.com/deviantony/docker-elk>项目，考虑到skywalking的兼容性，使用的es7版本

如需修改可参考该项目修改，logstash采用tcp的方式收集日志，logstash的地址在子应用由以下方式配置

```yaml
logging:
  # 配置logstash tcp地址
  stash:
    address: ${LOGSTASH_ADDRESS:localhost:5000}
```

> ps: 如果本地es存储空间不足，可以启用以下配置,允许写入

```
PUT /_all/_settings
{
  "index": {
    "blocks": {
      "read_only_allow_delete": "false"
    }
  }
}
```

## skywalking

skywalking-oap为服务

由于skywalking需要依赖nacos和elasticsearch,这里使用了wait-it-for脚本等待es启动成功，覆盖了容器默认的entrypoint，大家也可自行更改，如遇到启动失败，单独启动skywalking-oap

```shell
docker-compose -f ./docker-compose.yml restart skywalking-oap
```

skywalking-ui为界面,默认端口8080

子应用使用skywalking的探针方式，在子应用使用jib构建docker时已自动将skywalking agent加入到镜像中，启动时会自动加载agent，不需要其他额外配置，
如果需要添加额外插件，按照官方方式将插件放入plugins目录即可

也可修改[application.yml](./skywalking/application.yml)配置，已映射配置文件

# docker-prometheus.yml说明

包含以下export:

- node-exporter
- cadvisor
- mysqld-exporter
- redis_exporter
- elasticsearch-exporter

更新export查看<https://prometheus.io/docs/instrumenting/exporters/>自行加入

prometheus配置详情看[prometheus.yml](./prometheus/prometheus.yml),不过多赘述

rules下为Prometheus的alert配置，alertManager.yml邮件修改为自己的

# docker-matrix.yml说明

主要有以下几点：

- nacos的配置

在项目的application中通过环境变量传递

NACOS_SERVER_ADDRESS: host:port
NACOS_USERNAME: username
NACOS_PASSWD: password

- datasource配置

DATASOURCE_HOST: host:port
DATASOURCE_NAME: dbName
DATASOURCE_USERNAME: username
DATASOURCE_PASSWORD: password

- logstash的配置

LOGSTASH_ADDRESS: host:port

- skywalking配置

SW_AGENT_NAME: 默认构建时的项目名，如gateway
SW_AGENT_COLLECTOR_BACKEND_SERVICES: host:port

以上配置项都可在环境变量中修改

在本地开发环境中，由于我使用的是window+docker desktop+wsl2的方式，网络使用桥接模式，会导致子应用注册到nacos的ip为容器ip，
除docApp的swagger文档无法，其他不影响使用，所有应用都在一个网络，内部访问不影响

### grafana dashboard

下面是我使用grafana的数据面板，大家也可以自行导入自己的，更多查看<https://grafana.com/grafana/dashboards>

在import中导入以下ID

- node:1860
- docker:893
- redis:11835
- mysql:7362
- elasticsearch:6483
- JVM:4701
- springboot:11378

## 部分截图展示

- nacos
  ![nacos.png](img%2Fnacos.png)

- sentinel
  ![sentinel.png](img%2Fsentinel.png)

- seata
  ![seata.png](img%2Fseata.png)

- prometheus
  ![prometheus.png](img%2Fprometheus.png)