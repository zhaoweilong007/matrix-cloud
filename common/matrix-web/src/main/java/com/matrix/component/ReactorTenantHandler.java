package com.matrix.component;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.matrix.properties.TenantProperties;
import com.matrix.utils.ReactiveTenantCTH;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.schema.Column;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 17:33
 **/
@RequiredArgsConstructor
public class ReactorTenantHandler implements TenantLineHandler {

    private final TenantProperties tenantProperties;


    @Override
    public Expression getTenantId() {
        AtomicReference<Long> tenantId = new AtomicReference<>();
        ReactiveTenantCTH.getTenantId().subscribe(tenantId::set);
        if (tenantId.get() == null) {
            return new NullValue();
        }
        return new LongValue(tenantId.get());
    }

    @Override
    public String getTenantIdColumn() {
        return TenantProperties.TENANT_KEY;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        AtomicReference<Boolean> flag = new AtomicReference<>();
        ReactiveTenantCTH.isIgnore().subscribe(flag::set);
        return flag.get() || tenantProperties.getIgnoreTables().stream().anyMatch(s -> s.equalsIgnoreCase(tableName));
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
