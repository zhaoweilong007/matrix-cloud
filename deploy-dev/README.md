# 部署说明文档

## nacos

- 版本 v3.1.1
- ui地址：localhost:8848
- 默认账号 nacos/nacos

单机部署使用外部mysql。

MySQL 初始化 `nacos_config` 数据库，导入 [mysql-init/nacos.sql](./mysql-init/nacos.sql) 初始化数据库。

修改 [env/nacos-standlone-mysql.env](./env/nacos-standlone-mysql.env) 环境配置，其中 mysql 相关配置改成自己对应的。

### Docker 部署

通过 docker-compose.yml 启动（依赖 mysql 健康检查）：

```shell
docker-compose up -d nacos
```

> 注意⚠️：Nacos 2.0+ 版本新增了 gRPC 通信方式，除了主端口(8848)外，还需要开放 9848、9849 端口。
> 如果客户端和服务端之间存在端口转发或防火墙，需要做相应调整。

## sentinel

- 版本：1.8.5
- ui地址：localhost:8088
- 默认账号密码 sentinel/sentinel

配置 nacos 作为数据源，规则持久化到 nacos。

> ps: 在 gateway 网关集成 sentinel 时，需要添加 JVM 参数 `-Dcsp.sentinel.app.type=1`，将应用识别为网关，否则看不到 api 管理页面

## seata

### Seata 2.5.0

- ui地址：localhost:7091
- 默认账号：seata/seata

### 服务端配置

[seata/application.yml](./seata/application.yml) 使用 nacos 作为配置/注册中心，db 存储模式。

在 nacos 新增 data-id 为 `seataServer.properties`，group 为 `SEATA_GROUP` 的配置文件。

[seata/seataServer.properties](./seata/seataServer.properties) 包含传输、客户端、存储、服务端等全部配置参数。

Seata 配置初始化由 `seata-config-init` 容器自动完成（使用 [seata/nacos-config.sh](./seata/nacos-config.sh) 推送配置到 nacos）。

使用 db 模式，MySQL 自动初始化 [mysql-init/seata.sql](./mysql-init/seata.sql) 创建 seata 数据库和表。

这里默认使用 AT 模式。

### 客户端配置

seata 分组默认不配置，默认以 `spring.application.name` 值 + `-seata-service-group` 拼接后的字符串作为分组名。

会从 nacos 读取分组名对应的 seata-server 的集群名称，seata-server 的集群名称在 application.yml 中 `seata.register.nacos.cluster` 进行配置，默认为 default。

比如 system-server 服务，默认 seata 分组名为：`service.vgroupMapping.system-server-seata-service-group`

需要手动在 nacos 增加 dataid 为 `system-server-seata-service-group`，group 为 `SEATA_GROUP` 的配置文件，类型为 text，值对应的就是 seata-server 的集群名称 `default`。

## elasticSearch

- 版本：7.17.10

通过 docker-prometheus.yml 部署，单节点模式，xpack 安全认证已启用。

初始密码在 [.env](./.env) 中配置（ELASTIC_PASSWORD, LOGSTASH_INTERNAL_PASSWORD, KIBANA_SYSTEM_PASSWORD）。

用户初始化由 setup 容器自动完成。

> ps: 如果本地 ES 存储空间不足，可以启用以下配置允许写入：
>
> ```
> PUT /_all/_settings
> {
>   "index": {
>     "blocks": {
>       "read_only_allow_delete": "false"
>     }
>   }
> }
> ```

## SkyWalking

- APM 版本：9.6.0
- ui地址：localhost:8080
- java agent：项目根目录 skywalking-agent/，Jib 构建时自动注入镜像

### APM 部署

通过 docker-prometheus.yml 部署（sky-oap + sky-ui），OAP 使用 nacos 集群、elasticsearch 存储。

[skywalking/config/application.yml](./skywalking/config/application.yml) 配置 nacos 集群发现和 ES 存储。

### Agent 配置

注意 gateway 应用的 agent 和普通应用 servlet 的 agent 要分开：

gateway 应用的 agent 需要添加以下两个插件（从 optional-plugins 复制到 plugins）：
- apm-spring-cloud-gateway 对应版本的插件
- apm-spring-webflux 对应版本的插件

应用使用 agent 代理：

```shell
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar -jar yourApp.jar
```

需要在环境变量中增加：
- `SW_AGENT_NAME=yourAppName` — 应用名称
- `SW_AGENT_COLLECTOR_BACKEND_SERVICES=host:11800` — OAP 后端地址

如果需要开启 SQL 参数显示，修改 agent.config 将 `plugin.jdbc.trace_sql_parameters` 设置为 true。

## rocketmq

- 版本：5.3.4

当前在 docker-compose.yml 中已注释，需要时取消注释即可启用。

[rocketmq/broker1/conf/broker.conf](./rocketmq/broker1/conf/broker.conf) 为 broker 配置。

## XXL-Job

- 版本：3.3.1
- ui地址：localhost:8090/xxl-job-admin
- 默认账号：admin/123456

通过 docker-compose.yml 部署，依赖 mysql 健康检查。

[xxl-job/application.properties](./xxl-job/application.properties) 配置 mysql 连接和访问令牌。

## other

- gateway api endpoints:
  - `/actuator/gateway/routes/{id}` DELETE — 删除单个路由
  - `/actuator/gateway/routes/{id}` POST — 增加单个路由
  - `/actuator/gateway/routes/{id}` GET — 查看单个路由
  - `/actuator/gateway/routes` GET — 获取路由列表
  - `/actuator/gateway/refresh` POST — 路由刷新
  - `/actuator/gateway/globalfilters` GET — 获取全局过滤器列表
  - `/actuator/gateway/routefilters` GET — 路由过滤器工厂列表
  - `/actuator/gateway/routes/{id}/combinedfilters` GET — 获取单个路由的联合过滤器
