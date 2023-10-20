package com.matrix.datapermission.rule;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.context.TerminalContextHolder;
import com.matrix.common.entity.BaseEntity;
import com.matrix.common.enums.PlatformUserTypeEnum;
import com.matrix.common.enums.RoleEnum;
import com.matrix.common.model.RoleDTO;
import com.matrix.common.model.login.LoginUser;
import com.matrix.mybatis.utils.MyBatisUtils;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/25
 **/
public abstract class AbstractDataPermissionRule implements DataPermissionRule {

    static final Expression EXPRESSION_NULL = new NullValue();
    public final Set<String> TABLE_NAMES = new HashSet<>();
    public final Map<String, String> COLUMNS = new HashMap<>();

    public void addTable(Class<? extends BaseEntity> entityClass) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        TABLE_NAMES.add(tableName);
        COLUMNS.put(tableName, getColumnName());
    }

    public void addTable(String tableName) {
        TABLE_NAMES.add(tableName);
        COLUMNS.put(tableName, getColumnName());
    }

    public void addColumn(Class<? extends BaseEntity> entityClass, String column) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        TABLE_NAMES.add(tableName);
        COLUMNS.put(tableName, column);
    }

    public void addColumn(String tableName, String column) {
        COLUMNS.put(tableName, column);
        TABLE_NAMES.add(tableName);
    }

    /**
     * 获得字段名称
     *
     * @return {@link String}
     */
    public abstract String getColumnName();

    @Override
    public Set<String> getTableNames() {
        return TABLE_NAMES;
    }


    @Override
    public Expression getExpression(String tableName, Alias tableAlias) {
        final LoginUser loginUser = LoginUserContextHolder.getUser();
        // 只有用户登陆的情况下，才进行数据权限的处理
        if (loginUser == null) {
            return null;
        }
        //只有用户类型为B端用户类型的进行数据处理
        final PlatformUserTypeEnum userType = TerminalContextHolder.getUserType();
        if (ObjectUtils.notEqual(userType, PlatformUserTypeEnum.SYS_USER)) {
            return null;
        }
        //查询用户角色 无角色不处理 只有B端小程序自助注册的情况无角色 (就算是B端无角色 有租户校验+接口鉴权的情况下也无法查询到数据 所以直接放行
        final List<RoleDTO> roles = loginUser.getRoles();
        if (CollUtil.isEmpty(roles)) {
            return null;
        }
        //是否是管理员 管理员则不进行处理
        final Optional<RoleDTO> optional = StreamEx.of(roles)
                .findFirst(roleDTO -> Objects.equals(RoleEnum.ADMIN.getRoleKey(), roleDTO.getRoleKey()));
        if (optional.isPresent()) {
            return null;
        }
        final Expression expression = buildExpression(tableName, tableAlias, loginUser);
        if (expression == null) {
            return EXPRESSION_NULL;
        }
        return expression;
    }

    /**
     * 构建表达式 （按子类实现自定义
     * 默认最简单的情况equals
     *
     * @param tableName  表名
     * @param tableAlias 表别名
     * @param loginUser  登录用户
     * @return {@link Expression}
     */
    protected Expression buildExpression(String tableName, Alias tableAlias, LoginUser loginUser) {
        final String column = COLUMNS.get(tableName);
        return new EqualsTo(MyBatisUtils.buildColumn(tableName, tableAlias, column), new LongValue(getColumnValue(loginUser)));
    }

    protected abstract Long getColumnValue(LoginUser loginUser);

}
