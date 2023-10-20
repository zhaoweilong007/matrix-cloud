package com.matrix.datapermission.config;

/**
 * 额外自定义配置实现 需各微服务自行实现
 *
 * @author ZhaoWeiLong
 * @since 2023/8/25
 **/

public interface PermissionRuleCustomizer<PR> {
    /**
     * 定制额外规则
     *
     * @param rule 规则
     */
    void customize(PR rule);
}
