package com.matrix.teannt;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.matrix.properties.TenantProperties;
import com.matrix.utils.TenantContextHold;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.schema.Column;

import java.util.List;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 17:33
 **/
@RequiredArgsConstructor
public class PreTenantHandler implements TenantLineHandler {

    private final TenantProperties tenantProperties;


    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContextHold.getTenantId();
        if (tenantId == null) {
            return new NullValue();
        }
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return TenantProperties.TENANT_KEY;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return tenantProperties.getIgnoreTables().stream().anyMatch(s -> s.equalsIgnoreCase(tableName));
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
