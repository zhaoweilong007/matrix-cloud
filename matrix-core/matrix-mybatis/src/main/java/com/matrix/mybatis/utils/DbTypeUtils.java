package com.matrix.mybatis.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.matrix.mybatis.enums.DbTypeEnum;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBC 工具类，提供数据库连接检测和运行时数据库类型识别。
 *
 * @author matrix
 */
public class DbTypeUtils {

    private static final Logger log = LoggerFactory.getLogger(DbTypeUtils.class);

    /**
     * 默认数据库类型（未检测到时回退为 MySQL）
     */
    private static final DbType DEFAULT_DB_TYPE = DbType.MYSQL;

    private static volatile DbType cachedDbType;

    /**
     * 判断是否为 SQL Server
     *
     * @param url JDBC URL
     * @return 是否为 SQL Server
     */
    public static boolean isSQLServer(String url) {
        return StrUtil.containsAnyIgnoreCase(url, "sqlserver", "mssql");
    }

    /**
     * 判断是否为 SQL Server
     *
     * @param dbType 数据库类型
     * @return 是否为 SQL Server
     */
    public static boolean isSQLServer(DbType dbType) {
        return dbType == DbType.SQL_SERVER || dbType == DbType.SQL_SERVER2005;
    }

    /**
     * 从 JDBC URL 解析数据库类型
     *
     * @param url JDBC URL
     * @return 数据库类型
     */
    public static DbType getDbType(String url) {
        if (StrUtil.isBlank(url)) {
            return DEFAULT_DB_TYPE;
        }
        try {
            DbType dbType = JdbcUtils.getDbType(url);
            return dbType != null ? dbType : DEFAULT_DB_TYPE;
        } catch (Exception e) {
            log.debug("Failed to parse DbType from URL: {}", url, e);
            return DEFAULT_DB_TYPE;
        }
    }

    /**
     * 获取当前运行时的数据库类型。
     * 优先从 DataSource 连接元数据获取，失败回退为 MYSQL。
     *
     * @param dataSource 数据源
     * @return 数据库类型
     */
    public static DbType getDbType(DataSource dataSource) {
        if (dataSource == null) {
            return DEFAULT_DB_TYPE;
        }
        try (java.sql.Connection conn = dataSource.getConnection()) {
            String productName = conn.getMetaData().getDatabaseProductName();
            DbTypeEnum dbTypeEnum = DbTypeEnum.findByProductName(productName);
            if (dbTypeEnum != null) {
                return dbTypeEnum.getMpDbType();
            }
            // 回退：用 MyBatis-Plus 内置工具通过 URL 解析
            String url = conn.getMetaData().getURL();
            return getDbType(url);
        } catch (Exception e) {
            log.debug("Failed to detect DbType from DataSource, fallback to MYSQL", e);
            return DEFAULT_DB_TYPE;
        }
    }

    /**
     * 获取缓存的数据库类型
     *
     * @param dataSource 数据源（缓存未命中时用于检测）
     * @return 数据库类型
     */
    public static DbType getCachedDbType(DataSource dataSource) {
        if (cachedDbType != null) {
            return cachedDbType;
        }
        synchronized (DbTypeUtils.class) {
            if (cachedDbType == null) {
                cachedDbType = getDbType(dataSource);
            }
        }
        return cachedDbType;
    }
}
