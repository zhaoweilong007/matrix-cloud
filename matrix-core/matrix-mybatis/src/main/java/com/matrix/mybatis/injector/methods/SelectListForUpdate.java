package com.matrix.mybatis.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * MyBatis-Plus 自定义注入方法，提供 {@code selectListForUpdate} 查询。
 * <p>
 * 生成的 SQL 在原有查询语句末尾追加 {@code FOR UPDATE}，
 * 用于对查询结果集加行级锁，适用于悲观锁场景。
 */
public class SelectListForUpdate extends AbstractMethod {

    private static final String MAPPER_METHOD = "selectListForUpdate";
    private static final String SQL_TEMPLATE = "<script>%s SELECT %s FROM %s %s %s for update\n</script>";

    public SelectListForUpdate() {
        super("selectListForUpdate");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = String.format(
                SQL_TEMPLATE,
                sqlFirst(),
                sqlSelectColumns(tableInfo, true),
                tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo),
                sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, MAPPER_METHOD, sqlSource, tableInfo);
    }
}
