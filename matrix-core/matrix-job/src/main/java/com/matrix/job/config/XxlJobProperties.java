package com.matrix.job.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XXL-JOB 配置属性。
 *
 * @author matrix
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /** 调度中心地址 */
    private String adminAddresses;

    /** 访问令牌 */
    private String accessToken;

    /** 执行器配置 */
    private Executor executor = new Executor();

    @Data
    public static class Executor {
        /** 执行器 AppName */
        private String appName = "matrix-executor";
        /** 执行器 IP（为空则自动获取） */
        private String ip;
        /** 执行器端口（为空则自动计算 = server.port + 200） */
        private Integer port;
        /** 执行器日志路径 */
        private String logPath = "/data/applogs/xxl-job/jobhandler";
        /** 日志保留天数 */
        private int logRetentionDays = 30;
    }
}
