package com.matrix.mybatis.typehandler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * Set&lt;Long&gt; 逗号分隔类型处理器。
 *
 * <p>将 Set&lt;Long&gt; 序列化为逗号分隔字符串存入 VARCHAR 字段，
 * 读取时自动解析还原并去重。</p>
 *
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Set.class)
public class LongSetTypeHandler extends BaseTypeHandler<Set<Long>> {

    private static final String COMMA = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<Long> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, CollUtil.join(parameter, COMMA));
    }

    @Override
    public Set<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return stringToLongSet(rs.getString(columnName));
    }

    @Override
    public Set<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToLongSet(rs.getString(columnIndex));
    }

    @Override
    public Set<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToLongSet(cs.getString(columnIndex));
    }

    private Set<Long> stringToLongSet(String value) {
        if (StrUtil.isBlank(value)) {
            return Collections.emptySet();
        }
        Set<Long> result = new LinkedHashSet<>();
        for (String part : StrUtil.split(value, COMMA)) {
            try {
                result.add(Long.parseLong(part.trim()));
            } catch (NumberFormatException ignored) {
                // 跳过非数字值
            }
        }
        return result;
    }
}
