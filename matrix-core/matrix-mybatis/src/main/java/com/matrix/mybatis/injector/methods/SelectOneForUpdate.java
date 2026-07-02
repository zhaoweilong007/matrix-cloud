package com.matrix.mybatis.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * MyBatis-Plus 自定义注入方法，提供 {@code selectOneForUpdate} 查询。
 * <p>
 * 生成的 SQL 在原有查询语句末尾追加 {@code FOR UPDATE}，
 * 用于对单条查询结果加行级锁，适用于悲观锁场景。
 */
public class SelectOneForUpdate extends AbstractMethod {

    private static final String MAPPER_METHOD = "selectOneForUpdate";
    private static final String SQL_TEMPLATE = "<script>%s SELECT %s FROM %s %s %s for update\n</script>";

    public SelectOneForUpdate() {
        super("selectOneForUpdate");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlSource sqlSource = languageDriver.createSqlSource(
                configuration,
                String.format(
                        SQL_TEMPLATE,
                        sqlFirst(),
                        sqlSelectColumns(tableInfo, true),
                        tableInfo.getTableName(),
                        sqlWhereEntityWrapper(true, tableInfo),
                        sqlComment()),
                modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, MAPPER_METHOD, sqlSource, tableInfo);
    }
}
