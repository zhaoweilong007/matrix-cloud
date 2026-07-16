package com.matrix.tenant.core.db;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.matrix.auto.properties.TenantProperties;
import com.matrix.common.context.TenantContextHolder;
import java.util.Set;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 基于 MyBatis Plus 多租户的功能，实现 DB 层面的多租户的功能
 */
public class TenantDatabaseInterceptor implements TenantLineHandler {

    private final Set<String> ignoreTables;

    /**
     * 构造方法，初始化忽略租户的表集合
     *
     * @param properties 租户配置属性
     */
    public TenantDatabaseInterceptor(TenantProperties properties) {
        ignoreTables = properties.getIgnoreTables();
        // DUAL 表通过 ignoreTable 大小写不敏感判断处理，无需手动添加
    }

    /**
     * 获取当前租户 ID 表达式
     *
     * @return 租户 ID 的 SQL 表达式
     */
    @Override
    public Expression getTenantId() {
        return new LongValue(TenantContextHolder.getRequiredTenantId());
    }

    /**
     * 判断指定表是否忽略租户过滤
     *
     * @param tableName 表名
     * @return 是否忽略
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return TenantContextHolder.isIgnore() // 情况一，全局忽略多租户
                || CollUtil.contains(ignoreTables, tableName) // 情况二，忽略多租户的表
                || "dual".equalsIgnoreCase(tableName); // 情况三，Oracle DUAL 表大小写不敏感处理
    }
}
