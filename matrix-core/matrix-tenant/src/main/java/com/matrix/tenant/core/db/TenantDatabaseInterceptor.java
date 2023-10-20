package com.matrix.tenant.core.db;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.matrix.auto.properties.TenantProperties;
import com.matrix.common.context.TenantContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

import java.util.Set;

/**
 * 基于 MyBatis Plus 多租户的功能，实现 DB 层面的多租户的功能
 */
public class TenantDatabaseInterceptor implements TenantLineHandler {

    private final Set<String> ignoreTables;


    public TenantDatabaseInterceptor(TenantProperties properties) {
        ignoreTables = properties.getIgnoreTables();
        ignoreTables.add("dual");
        ignoreTables.add("DUAL");
        ignoreTables.add("ams_sys_user");
        ignoreTables.add("ums_user_basic");
    }

    @Override
    public Expression getTenantId() {
        return new LongValue(TenantContextHolder.getRequiredTenantId());
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return TenantContextHolder.isIgnore() // 情况一，全局忽略多租户
                || CollUtil.contains(ignoreTables, tableName); // 情况二，忽略多租户的表
    }

}
