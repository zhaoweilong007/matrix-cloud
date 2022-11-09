package com.matrix.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/6/20 15:25
 **/
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
@Data
public class XxlJobProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PREFIX = "matrix.xxl-job";

    /**
     * 是否开启xxl-job
     */
    private Boolean enable = false;

    /**
     * 调度中心地址
     */
    private String adminAddresses = "http://localhost:8090/xxl-job-admin";

    private String accessToken = "default_token";

    /**
     * 执行器AppName[选填]:执行器心跳注册分组依据,为空则关闭自动注册(xxl-job executor app name)
     */
    @Value("${spring.application.name}")
    private String executorAppName;

    /**
     * 执行器注册[选填]:优先使用该配置作为注册地址,为空时使用内嵌服务 IP:PORT 作为注册地址
     * 从而更灵活的支持容器类型执行器动态IP和动态映射端口问题
     */
    private String executorAddress;


    /**
     * 执行器IP[选填]:默认为空表示自动获取IP,多网卡时可手动设置指定IP,该IP不会绑定Host仅作为通讯实用.
     * 地址信息用于'执行器注册'和'调度中心请求并触发任务'.
     */
    private String executorIp;

    /**
     * 执行器端口号[选填]:小于等于0则自动获取,默认端口为9999,单机部署多个执行器时,注意要配置不同执行器端口.
     */
    private Integer executorPort = 0;

    /**
     * 执行器运行日志文件存储磁盘路径[选填]:需要对该路径拥有读写权限,为空则使用默认路径.
     */
    private String executorLogPath = "/var/xxl-job/logs";

    /**
     * 执行器日志文件保存天数[选填],过期日志自动清理,限制值大于等于3时生效;否则,如-1,关闭自动清理功能.
     */
    private Integer executorLogRetentionDays = 30;

}
