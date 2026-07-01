package com.matrix.mybatis.enums;

import com.baomidou.mybatisplus.annotation.DbType;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据库类型枚举，提供跨数据库兼容的 SQL 模板。
 *
 * <p>核心能力：为不同数据库提供 FIND_IN_SET 等效 SQL 表达式，
 * 使业务代码无需关心底层数据库差异。</p>
 *
 * @author matrix
 */
public enum DbTypeEnum {

    H2(DbType.H2, "H2", "POSITION(',' || CAST(#{value} AS VARCHAR) || ',' IN ',' || #{column} || ',') > 0"),
    MY_SQL(DbType.MYSQL, "MySQL", "FIND_IN_SET(#{value}, #{column}) <> 0"),
    ORACLE(DbType.ORACLE, "Oracle", "INSTR(',' || #{column} || ',', ',' || #{value} || ',') > 0"),
    POSTGRE_SQL(DbType.POSTGRE_SQL, "PostgreSQL",
            "POSITION(',' || CAST(#{value} AS VARCHAR) || ',' IN ',' || #{column} || ',') > 0"),
    SQL_SERVER(DbType.SQL_SERVER, "Microsoft SQL Server",
            "CHARINDEX(',' + CAST(#{value} AS varchar(255)) + ',', ',' + #{column} + ',') > 0"),
    DM(DbType.DM, "DM DBMS", "FIND_IN_SET(#{value}, #{column}) <> 0"),
    KINGBASE_ES(DbType.KINGBASE_ES, "KingbaseES",
            "POSITION(',' || CAST(#{value} AS VARCHAR) || ',' IN ',' || #{column} || ',') > 0"),
    OCEAN_BASE(DbType.OCEAN_BASE, "OceanBase", "FIND_IN_SET(#{value}, #{column}) <> 0");

    private final DbType mpDbType;
    private final String productName;
    private final String findInSetTemplate;

    DbTypeEnum(DbType mpDbType, String productName, String findInSetTemplate) {
        this.mpDbType = mpDbType;
        this.productName = productName;
        this.findInSetTemplate = findInSetTemplate;
    }

    public DbType getMpDbType() {
        return mpDbType;
    }

    public String getProductName() {
        return productName;
    }

    public String getFindInSetTemplate() {
        return findInSetTemplate;
    }

    private static final Map<DbType, DbTypeEnum> MP_DB_TYPE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(DbTypeEnum::getMpDbType, Function.identity()));

    private static final Map<String, DbTypeEnum> PRODUCT_NAME_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(DbTypeEnum::getProductName, Function.identity()));

    public static DbTypeEnum findByMpDbType(DbType mpDbType) {
        return MP_DB_TYPE_MAP.get(mpDbType);
    }

    public static DbTypeEnum findByProductName(String productName) {
        return PRODUCT_NAME_MAP.get(productName);
    }

    /**
     * 获取 FIND_IN_SET SQL 模板（静态工具方法）。
     *
     * @param dbType MyBatis-Plus 数据库类型
     * @return FIND_IN_SET SQL 模板
     */
    public static String findTemplate(DbType dbType) {
        DbTypeEnum dbTypeEnum = findByMpDbType(dbType);
        if (dbTypeEnum != null) {
            return dbTypeEnum.getFindInSetTemplate();
        }
        return MY_SQL.getFindInSetTemplate();
    }
}
