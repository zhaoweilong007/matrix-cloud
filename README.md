# Matrix-Cloud

## ✨介绍

Matrix-Cloud是微服务的脚手架，整合目前主流的微服务框架

### 🔨项目环境

开发环境和相应中间件版本

- jdk17
- gradle8.2

### 📝技术选型

| 技术框架                 | 描述                   | 版本               |
|----------------------|----------------------|------------------|
| spring cloud alibaba | spring cloud alibaba | 2022.0.0.0       |
| spring cloud         | spring cloud         | 2022.0.4         |
| spring boot          | spring boot          | 3.1.10           |
| nacos                | 服务注册发现               | 2.2.1            |
| spring cloud gateway | 网关                   | 依赖spring cloud版本 |
| sentinel             | 熔断限流                 | 1.8.5            |
| sa-token             | 权限认证                 | 1.37.0           |
| seata                | 分布式事务                | 1.7.1            |
| rocketmq             | 消息队列                 | 4.9.4            |
| skywalking           | 分布式链路追踪              | 9.2.0            |
| ELK                  | 日志处理分析               | 7.17.6           |
| prometheus           | 应用监控                 | latest           |

基础功能已开发完毕，可自定义子应用接入spring cloud matrix微服务，详情请看[#构建自定义组件说明](#构建自定义组件说明)

## 📌模块

| 模块              | 描述                | 服务地址                                  | 默认用户密码            |
|-----------------|-------------------|---------------------------------------|-------------------|
| nacos           | 注册中心              | http://localhost:8848/nacos           | nacos/nacos       |
| sentinel        | 流量卫兵              | http://localhost:8088/dashboard       | sentinel/sentinel |
| seata           | 分布式事务             | http://localhost:7091/TransactionInfo | seata/seata       |
| skyWalking      | 链路追踪              | http://localhost:8080/general         | 无                 |
| elasticSearch   | 搜索引擎              | http://localhost:9200                 | elastic/changeme  |
| kibana          | 日志分析              | http://locahost:5601                  | elastic/changeme  |
| prometheus      | 监控                | http://localhost:9090                 | 无                 |
| grafana         | 监控展示              | http://localhost:3000                 | admin/admin       |
| xxl-job-admin   | 分布式任务调度           | http://localhost:8090/xxl-job-admin   | admin/123456      |
| rocketmqConsole | mq控制台             | http://localhost:19876/               | 无                 |
| matrix-gateway  | 网关                | http://localhost:9000                 | 无                 |
| matrix-admin    | spring boot admin | http://localhost:9001                 | admin/admin       |
| matrix-system   | 系统服务              | http://localhost:9002                 |                   |
| matrix-resource | OSS、SMS、Email     | http://localhost:9003                 | 无                 |

## ⏳功能开发进度

- [x] RBAC权限管理
- [x] 聚合swagger文档
- [x] 多租户管理
- [x] 动态路由
- [x] 灰度发布
- [x] 集成seata分布式事务
- [x] 集成skyWalking分布式链路追踪
- [x] 集成jib构建docker
- [x] 集成prometheus监控
- [x] 集成ELK日志收集
- [x] 集成分布式任务xxl-job
- [x] 集成rocketmq消息队列
- [ ] 集成sharding-jdbc分库分表
- [ ] 集成工作流flowable

## 公共组件说明

- [matrix-bom](matrix-bom)

依赖统一版本管理，具体版本号请看：[version.gradle](version.gradle)

- [matrix-api](matrix-core%2Fmatrix-api)

子服务api接口依赖，提供子服务间调用基础功能

- [matrix-auth](matrix-core%2Fmatrix-auth)

权限认证相关功能

- [matrix-auto](matrix-core%2Fmatrix-auto)

相关配置类，其他自动装配组件使用下引用

- [matrix-common](matrix-core%2Fmatrix-common)

> 公共组件，提供基础的工具类和通用的工具类

- [matrix-config](matrix-core%2Fmatrix-config)

公共的nacos配置，默认自动读取[bootstrap.properties](matrix-core%2Fmatrix-config%2Fsrc%2Fmain%2Fresources%2Fbootstrap.properties)
可通过环境变量指定

- [matrix-data-permission](matrix-core%2Fmatrix-data-permission)

数据权限相关功能，基于注解的数据权限隔离

- [matrix-es](matrix-core%2Fmatrix-es)

集成elasticSearch，提供es的相关操作

- [matrix-excel](matrix-core%2Fmatrix-excel)

通用的excel操作，提供导入导出功能

- [matrix-feign](matrix-core%2Fmatrix-feign)

集成openfeign，支持版本号负载均衡

- [matrix-idempotent](matrix-core%2Fmatrix-idempotent)

幂等性校验

- [matrix-job](matrix-core%2Fmatrix-job)

集成xxl-job，提供分布式任务调度功能

- [matrix-jpush](matrix-core%2Fmatrix-jpush)

集成极光推送，提供推送功能

- [matrix-lock](matrix-core%2Fmatrix-lock)

分布式锁相关功能

- [matrix-log](matrix-core%2Fmatrix-log)

公共日志配置，记录操作日志

- [matrix-mongodb](matrix-core%2Fmatrix-mongodb)

集成mongodb，提供mongodb的相关操作

- [matrix-mq](matrix-core%2Fmatrix-mq)

集成aliyun rocketMq，提供rocketmq的相关操作

- [matrix-mybatis](matrix-core%2Fmatrix-mybatis)

集成mybatis，提供mybatis的相关操作

- [matrix-oss](matrix-core%2Fmatrix-oss)

oss对象存储相关

- [matrix-prometheus](matrix-core%2Fmatrix-prometheus)

服务监控相关

- [matrix-redis](matrix-core%2Fmatrix-redis)

redis相关操作

- [matrix-seata](matrix-core%2Fmatrix-seata)

集成seata分布式事务

- [matrix-sensitive](matrix-core%2Fmatrix-sensitive)

敏感数据脱敏

- [matrix-sentinel](matrix-core%2Fmatrix-sentinel)

集成sentinel，提供限流、熔断、降级、热点key、系统保护等能力

支持动态读取nacos实现实时配置

- [matrix-sms](matrix-core%2Fmatrix-sms)

短信功能

- [matrix-strategy](matrix-core%2Fmatrix-strategy)

策略组件

- [matrix-swagger](matrix-core%2Fmatrix-swagger)

集成swagger，提供swagger文档聚合

- [matrix-tenant](matrix-core%2Fmatrix-tenant)

多租户组件

- [matrix-test](matrix-core%2Fmatrix-test)
  测试组件

- [matrix-translation](matrix-core%2Fmatrix-translation)
  翻译相关，字典翻译、字段id>name翻译


- [matrix-validator](matrix-core%2Fmatrix-validator)
  校验组件

- [matrix-web](matrix-core%2Fmatrix-web)

web组件，提供对servlet服务的支持及相关配置

- ps:

> 在SpringBoot 2.4.x的版本之后，对于bootstrap.properties/bootstrap.yaml配置文件 的支持
> Spring Cloud 2020版本以后，默认移除了对Netflix的依赖，其中就包括Ribbon，官方默认推荐使用Spring Cloud
> Loadbalancer正式替换Ribbon，并成为了Spring Cloud负载均衡器的唯一实现

## matrix-system

### 路由管理

- 基于nacos的配置，修改后可使路由实时生效

### 权限管理

- sa-token框架集成，网关统一鉴权，内部服务外网隔离
- 基于RBAC的权限管理，动态配置资源权限

## 🔔构建自定义组件说明

如需自定义组件，可按以下步骤自动接入spring cloud matrix服务

构建自定义组件作为spring cloud matrix下的子应用，参考以下步骤快速接入

以[demo示例](/demo)作为演示：

### 1、新建模块，添加依赖

- 在spring-cloud-matrix下新建模块
- 添加以下核心配置依赖

```groovy
dependencies {
    implementation(project(":matrix-common:matrix-core"))
    implementation(project(":matrix-common:matrix-web"))
}
```

matrix-core自动集成

matrix-web对servlet的配置（目前只支持servlet）

### 2、启用注解，增加配置

- 在启动类上加上`@EnableMatrix`注解
- 新建bootstrap.yml配置文件，配置如下

```yaml
spring:
  application:
    name: #服务名
  profiles:
    active: ${PROFILE:} #环境配置 对应nacos的namespace
  config:
    import:
      - optional:nacos:env.properties  #环境变量
      - optional:nacos:application-common.yml  #公共配置
      - optional:nacos:datasource.yml       #数据库配置
logging:
  # 配置logstash tcp地址
  stash:
    address: ${LOGSTASH_ADDRESS:localhost:5000}
  file:
    path: /var/logs
```

> 以上步骤完成可正常启动应用，自动接入所有微服务功能

### matrix 配置说明

默认配置说明

```yaml
matrix:
  security:
    # 验证码
    captcha:
      #需要校验的地址
      validateUrl:
      # - /auth/sys/login
  access-log: false  #是否记录访问日志
  load-balance:
    gray:
      enabled: true  #灰度负载均衡
      defaultVersion: 1.0  #默认兜底版本
      chooser: com.matrix.feign.chooser.ProfileRuleChooser #选择器
      ips:  #支持的ip
  swagger: #swagger文档配置
    enable: false
    name: #文档名称
    version:  #版本
    description:  #描述
  tenant:
    enable: false #是否开启多租户模式
    ignoreTables: # 多租户忽略的表名
    ignore-urls: # 多租户忽略的接口地址
  security:
    white-urls: # 接口白名单地址
  #设置不用key的过期时间
  cache:
    - name: #key名称
      ttl: #过期ttl
      prefix: #key前缀
```

以上是matrix的所有配置

## 部署

### 应用构建

所有应用使用jib自动构建docker镜像

项目使用阿里云镜像仓库，可以修改为其他仓库，在[build.gradle](/build.gradle)下修改

构建本地镜像并推送到远程仓库

```shell
./gradlew.bat jib
```

如新增自定义组件，还需在[build.gradle](/build.gradle)下以下位置增加一行配置，让该模块包含jib的配置

```groovy
//定义需要构建docker的模块
def javaMicroservices = [
        project(':matrix-gateway'),
        project(':matrix-system:system-biz'),
        //添加自定义组件
        project(':youModuleName')
]
```

### 中间件

[推荐]()使用docker-compose一键部署，具体部署说明请看请看[readme-docker](./deploy/README-docker.md)

中间件的单机部署可以查看[deploy文档](/deploy/README.md)

> ps: 如果是单机部署，sql文件需要自行导入，deploy下sql文件夹