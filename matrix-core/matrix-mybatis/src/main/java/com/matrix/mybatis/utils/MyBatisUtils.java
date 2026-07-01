package com.matrix.mybatis.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.matrix.mybatis.enums.DbTypeEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

/**
 * MyBatis 工具类
 */
public class MyBatisUtils {

    private static final String MYSQL_ESCAPE_CHARACTER = "`";

    /**
     * 将拦截器添加到链中 由于 MybatisPlusInterceptor 不支持添加拦截器，所以只能全量设置
     *
     * @param interceptor 链
     * @param inner       拦截器
     * @param index       位置
     */
    public static void addInterceptor(MybatisPlusInterceptor interceptor, InnerInterceptor inner, int index) {
        List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
        inners.add(index, inner);
        interceptor.setInterceptors(inners);
    }

    /**
     * 获得 Table 对应的表名
     * <p>
     * 兼容 MySQL 转义表名 `t_xxx`
     *
     * @param table 表
     * @return 去除转移字符后的表名
     */
    public static String getTableName(Table table) {
        String tableName = table.getName();
        if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
            tableName = tableName.substring(1, tableName.length() - 1);
        }
        return tableName;
    }

    /**
     * 构建 Column 对象
     *
     * @param tableName  表名
     * @param tableAlias 别名
     * @param column     字段名
     * @return Column 对象
     */
    public static Column buildColumn(String tableName, Alias tableAlias, String column) {
        if (tableAlias != null) {
            tableName = tableAlias.getName();
        }
        return new Column(tableName + StringPool.DOT + column);
    }

    // ========== 排序字段名安全校验 ==========

    /**
     * 合法排序字段名正则：仅允许字母、数字、下划线和点号
     */
    private static final Pattern SAFE_COLUMN_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*$");

    /**
     * 构建安全的排序字段名（驼峰转下划线 + SQL 注入防护）
     *
     * @param field 字段名
     * @return 安全的下划线格式字段名
     */
    public static String buildSafeOrderColumn(String field) {
        String column = StrUtil.toUnderlineCase(field);
        if (!SAFE_COLUMN_PATTERN.matcher(column).matches()) {
            throw new IllegalArgumentException("Invalid order column: " + field);
        }
        return column;
    }

    // ========== 分页构建 ==========

    /**
     * 构建 MyBatis-Plus Page 对象，支持排序字段。
     *
     * @param pageNum      页码
     * @param pageSize     每页大小
     * @param orderItem    排序项（可为 null）
     * @param <T>          数据类型
     * @return Page 对象
     */
    public static <T> Page<T> buildPage(long pageNum, long pageSize, OrderItem orderItem) {
        Page<T> page = new Page<>(pageNum, pageSize);
        if (orderItem != null) {
            String safeColumn = buildSafeOrderColumn(orderItem.getColumn());
            orderItem.setColumn(safeColumn);
            page.addOrder(orderItem);
        }
        return page;
    }

    /**
     * 构建 MyBatis-Plus Page 对象，支持多字段排序。
     *
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @param orderItems 排序项列表（可为 null 或空）
     * @param <T>        数据类型
     * @return Page 对象
     */
    public static <T> Page<T> buildPage(long pageNum, long pageSize, List<OrderItem> orderItems) {
        Page<T> page = new Page<>(pageNum, pageSize);
        if (CollUtil.isNotEmpty(orderItems)) {
            for (OrderItem orderItem : orderItems) {
                String safeColumn = buildSafeOrderColumn(orderItem.getColumn());
                orderItem.setColumn(safeColumn);
                page.addOrder(orderItem);
            }
        }
        return page;
    }

    // ========== 向 Wrapper 添加排序 ==========

    /**
     * 向 QueryWrapper 添加排序。
     *
     * @param wrapper      查询包装器
     * @param orderItems   排序项
     * @param <T>          数据类型
     */
    public static <T> void addOrder(QueryWrapper<T> wrapper, List<OrderItem> orderItems) {
        if (CollUtil.isEmpty(orderItems)) {
            return;
        }
        for (OrderItem orderItem : orderItems) {
            String safeColumn = buildSafeOrderColumn(orderItem.getColumn());
            wrapper.orderBy(true, orderItem.isAsc(), safeColumn);
        }
    }

    /**
     * 向 LambdaQueryWrapper 添加排序（通过拼接 ORDER BY 实现）。
     *
     * @param wrapper      查询包装器
     * @param orderItems   排序项
     * @param <T>          数据类型
     */
    public static <T> void addOrder(LambdaQueryWrapper<T> wrapper, List<OrderItem> orderItems) {
        if (CollUtil.isEmpty(orderItems)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (OrderItem orderItem : orderItems) {
            String safeColumn = buildSafeOrderColumn(orderItem.getColumn());
            if (sb.length() > 0) {
                sb.append(StringPool.COMMA);
            }
            sb.append(safeColumn).append(StringPool.SPACE);
            sb.append(orderItem.isAsc() ? "ASC" : "DESC");
        }
        wrapper.last("ORDER BY " + sb);
    }

    // ========== 跨数据库 FIND_IN_SET ==========

    /**
     * 跨数据库 FIND_IN_SET 实现。
     * 根据数据库类型自动选择对应的 SQL 表达式。
     *
     * @param dbType    数据库类型
     * @param column    字段名
     * @param value     查询值
     * @return FIND_IN_SET SQL 片段
     */
    public static String findInSet(DbType dbType, String column, Object value) {
        String template = DbTypeEnum.findTemplate(dbType);
        return template.replace("#{column}", column).replace("#{value}", String.valueOf(value));
    }

    /**
     * 跨数据库 FIND_IN_SET 实现（默认 MySQL 语法）。
     *
     * @param column    字段名
     * @param value     查询值
     * @return FIND_IN_SET SQL 片段
     */
    public static String findInSet(String column, Object value) {
        return findInSet(DbType.MYSQL, column, value);
    }

    // ========== 驼峰转下划线 ==========

    /**
     * 通过 Lambda 函数式引用获取字段名并转为下划线格式。
     *
     * <p>示例：{@code toUnderlineCase(UserEntity::getNickName)} → {@code "nick_name"}</p>
     *
     * @param func Lambda 字段引用
     * @param <T>  实体类型
     * @return 下划线格式的字段名
     */
    public static <T> String toUnderlineCase(SFunction<T, ?> func) {
        String fieldName = LambdaUtils.extract(func).getImplMethodName();
        // 去除 get/is 前缀
        if (fieldName.startsWith("get")) {
            fieldName = fieldName.substring(3);
        } else if (fieldName.startsWith("is")) {
            fieldName = fieldName.substring(2);
        }
        return StrUtil.toUnderlineCase(StrUtil.lowerFirst(fieldName));
    }
}
