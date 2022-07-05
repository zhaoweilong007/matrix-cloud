# Spring Cloud Matrix

## 介绍

Spring cloud matrix是微服务的矩阵，整合目前主流的微服务框架

### 项目环境

开发环境和相应中间件版本

- jdk11
- gradle7.4.2
- mysql8.0
- redis5.0
- elasticSearch8.0

### 技术选型

| 技术框架                 | 描述                   | 版本           |
|----------------------|----------------------|--------------|
| spring cloud alibaba | spring cloud alibaba | 2021.0.1.0   |
| spring cloud         | spring cloud         | 2021.0.1     |
| spring boot          | spring boot          | 2.6.3        |
| nacos                | 服务注册发现               | 2.1.0        |
| spring cloud gateway | 网关                   | 3.1.1        |
| sentinel             | 熔断限流                 | 1.8.4        |
| sa-Token             | 权限认证                 | 1.30.0       |
| seata                | 分布式事务                | 1.5.1        |
| rocketmq             | 消息队列                 | 4.9.4        |
| skywalking           | 分布式链路追踪              | 9.1.0        |
| ELK                  | 日志处理分析               | 8.0          |
| promethus+grafana    | 应用监控                 | 2.36.2+9.0.1 |

后续会逐步整合相关框架，搭建一个分布式系统架构.....

## Common组件

common组件包含以下公共依赖

- spring-boot-starter-web
- spring-boot-starter-actuator
- spring-cloud-starter-alibaba-nacos-discovery
- spring-cloud-starter-alibaba-nacos-config
- spring-cloud-starter-bootstrap

> 在SpringBoot 2.4.x的版本之后，对于bootstrap.properties/bootstrap.yaml配置文件 的支持

- spring-cloud-starter-openfeign
- spring-cloud-starter-loadbalancer

> Spring Cloud 2020版本以后，默认移除了对Netflix的依赖，其中就包括Ribbon，官方默认推荐使用Spring Cloud
> Loadbalancer正式替换Ribbon，并成为了Spring Cloud负载均衡器的唯一实现

- spring-cloud-starter-alibaba-sentinel

> 集成sentinel的支持

## Gateway网关

gateway使用alibaba sentinel集成，支持nacos动态路由配置

> ps:在gateway网关集成sentinel时，需要添加JVM参数`-Dcsp.sentinel.app.type=1`,将应用识别为网关，否则看不到api管理页面

通过nacos监听配置，使用`RouteDefinitionWriter`更新网关路由配置，实现动态路由配置

## 部署

相关中间件的部署可以查看[deploy文档](/deploy/README.md)