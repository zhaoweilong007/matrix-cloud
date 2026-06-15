# Docker 部署说明

本项目提供 docker 一键部署各中间件和微服务应用，deploy-dev 目录包含以下三个 compose 配置：

- `docker-compose.yml` — 基础中间件部署配置（mysql/redis/nacos/sentinel/seata/xxl-job）
- `docker-prometheus.yml` — 监控栈部署配置（elasticsearch/skywalking/prometheus/grafana/exporters）
- `app/docker-compose.yml` — 微服务应用部署配置（gateway/resource/system/admin）

以上三个配置公用 `matrix_cloud` 网络，这样的好处是开发时直接使用 hostname 访问，不需要频繁修改本地 IP。

- docker compose 的环境变量在 [.env](./.env) 中
- 各容器的环境变量可在 [env](./env) 文件夹下查看
- 微服务应用的环境变量在 [app/.env](./app/.env) 和 [app/dev.env](./app/dev.env) 中

# docker-compose.yml 说明

包含：

- mysql
- redis
- nacos
- sentinel
- seata-server + seata-config-init
- xxl-job-admin

> 具体版本查看 [.env 文件](./.env)，可自行更改，注意不同版本的配置可能有所不同

## mysql

MySQL 8.0.30，使用官方镜像 + 初始化脚本挂载方式。

[mysql-init/](./mysql-init/) 目录下的 SQL 文件会自动在首次启动时执行：

- [nacos.sql](./mysql-init/nacos.sql) — 创建 nacos_config 数据库
- [seata.sql](./mysql-init/seata.sql) — 创建 seata 数据库
- [tables_xxl_job.sql](./mysql-init/tables_xxl_job.sql) — 创建 xxl_job 数据库
- [matrix.sql](./mysql-init/matrix.sql) — 创建 matrix 业务数据库
- [undolog.sql](./mysql-init/undolog.sql) — 创建 undo_log 表（Seata AT 模式客户端）

默认端口 3306，默认 root 密码：matrix，默认创建用户：nacos/nacos

## redis

配置文件映射地址：[redis.conf](./redis/conf/redis.conf)

默认端口：6379，默认密码：matrix

> 如果提示无权限，需要给 redis/data 目录授予 777 权限

## nacos

依赖 mysql 健康检查，启动前确保 mysql 已就绪。

## sentinel

使用第三方镜像（自动集成 nacos 数据源），规则持久化到 nacos。

## seata-server

Seata 2.5.0，使用 nacos 配置/注册中心，db 存储模式。

`seata-config-init` 容器会自动将 [seataServer.properties](./seata/seataServer.properties) 推送到 nacos。

配置文件映射：
- [seata/application.yml](./seata/application.yml) — 服务端配置
- [seata/seataServer.properties](./seata/seataServer.properties) — Nacos 配置数据
- [seata/logback-spring.xml](./seata/logback-spring.xml) — 日志配置

默认用户密码：seata/seata

> ps: 由于使用 docker 部署，seata 注册到 nacos 的 IP 为容器 IP，通过 `SEATA_IP` 环境变量指定实际 IP

## xxl-job-admin

依赖 mysql 健康检查。配置文件：[application.properties](./xxl-job/application.properties)

# docker-prometheus.yml 说明

使用 `default` 网络（与 docker-compose.yml 的 `matrix_cloud` 网络隔离，通过 host 网络访问应用服务）。

包含：

- elasticsearch（单节点，xpack 安全认证）
- setup（ES 用户初始化容器，profile: setup）
- skywalking-oap + skywalking-ui
- prometheus + grafana
- node-exporter, cadvisor, mysqld-exporter, redis_exporter, alertmanager

## elasticsearch

Elasticsearch 7.17.10，单节点模式，安全认证已启用。

setup 容器会自动创建以下用户：
- logstash_internal, kibana_system, metricbeat_internal, filebeat_internal, heartbeat_internal, monitoring_internal, beats_system

配置文件：[elasticsearch/config/elasticsearch.yml](./elasticsearch/config/elasticsearch.yml)

> ps: 如果 ES data/logs 目录提示无权限，需要授予 777 权限

## skywalking

skywalking-oap 使用 nacos 集群发现、elasticsearch 存储。

配置文件映射：[skywalking/config/application.yml](./skywalking/config/application.yml)

skywalking-ui 默认端口 8080。

## prometheus

配置文件：[prometheus/prometheus.yml](./prometheus/prometheus.yml)

采集目标：
- prometheus, cadvisor, node-exporter, alertmanager
- mysqld-exporter, redis_exporter
- 应用服务（gateway/resource/system）的 `/actuator/prometheus` 端点

rules 下为 Prometheus alert 配置：
- [node_down.yml](./prometheus/rules/node_down.yml) — 实例/MySQL 宕机告警
- [galera.yml](./prometheus/rules/galera.yml) — MySQL Galera 集群告警

[alertmanager.yml](./prometheus/alertmanager.yml) 邮件配置需修改为自己的 SMTP 信息。

# app/docker-compose.yml 说明

微服务应用部署，包含：

- matrix-gateway（端口 9001）
- matrix-resource（端口 9003）
- matrix-system（端口 9004）
- matrix-admin（端口 9002）

每个服务自动加载 SkyWalking agent，挂载日志目录。

管理脚本：[app/docker.sh](./app/docker.sh) 支持 start/stop/restart（全部或单个服务）。

版本管理：[app/setenv.sh](./app/setenv.sh) 更新 VERSION 变量。

环境变量配置：
- [app/.env](./app/.env) — 镜像仓库地址、版本号、本地 IP
- [app/dev.env](./app/dev.env) — Nacos 地址、SkyWalking 地址、JVM 参数、调试端口

> ps: 在 WSL2 + Docker Desktop 环境下，应用注册到 nacos 的 IP 为容器 IP，
> 内部服务间通信不受影响（同一网络），但外部访问可能需要配置。

### grafana dashboard

在 import 中导入以下 ID（更多查看 https://grafana.com/grafana/dashboards）：

| 面板 | ID |
|------|-----|
| node | 1860 |
| docker | 893 |
| redis | 11835 |
| mysql | 7362 |
| elasticsearch | 6483 |
| JVM | 4701 |
| springboot | 11378 |

# 环境准备脚本

[script/](./script/) 目录提供主机环境准备脚本：

- [dockerSetup.sh](./script/dockerSteup.sh) — CentOS Docker + docker-compose 安装
- [repo.sh](./script/repo.sh) — CentOS yum 源替换为清华镜像
- [fishSetup.sh](./script/fishSteup.sh) — 安装 fish shell

# nginx 反向代理

[nginx/](./nginx/) 目录提供反向代理配置，代理到 gateway 服务。

启动脚本：[nginx/start.sh](./nginx/start.sh)

# filebeat 日志采集

[filebeat/](./filebeat/) 目录提供 filebeat 配置，采集业务日志输出到 Kafka。

重启脚本：[filebeat/fbeat_restart.sh](./filebeat/fbeat_restart.sh)

## 部分截图展示

- nacos
  ![nacos.png](img%2Fnacos.png)

- sentinel
  ![sentinel.png](img%2Fsentinel.png)

- seata
  ![seata.png](img%2Fseata.png)

- prometheus
  ![prometheus.png](img%2Fprometheus.png)
