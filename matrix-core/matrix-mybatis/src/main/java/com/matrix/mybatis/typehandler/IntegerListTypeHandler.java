package com.matrix.mybatis.typehandler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * List&lt;Integer&gt; 逗号分隔类型处理器。
 *
 * <p>将 List&lt;Integer&gt; 序列化为逗号分隔字符串存入 VARCHAR 字段，
 * 读取时自动解析还原。</p>
 *
 * @author matrix
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class IntegerListTypeHandler extends BaseTypeHandler<List<Integer>> {

    private static final String COMMA = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, CollUtil.join(parameter, COMMA));
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return stringToIntegerList(rs.getString(columnName));
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return stringToIntegerList(rs.getString(columnIndex));
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return stringToIntegerList(cs.getString(columnIndex));
    }

    private List<Integer> stringToIntegerList(String value) {
        if (StrUtil.isBlank(value)) {
            return Collections.emptyList();
        }
        List<String> parts = StrUtil.split(value, COMMA);
        List<Integer> result = new ArrayList<>(parts.size());
        for (String part : parts) {
            try {
                result.add(Integer.parseInt(part.trim()));
            } catch (NumberFormatException ignored) {
                // 跳过非数字值
            }
        }
        return result;
    }
}
