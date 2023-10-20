package com.matrix.datapermission.rule;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.google.common.collect.HashBasedTable;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.context.TerminalContextHolder;
import com.matrix.common.entity.BaseEntity;
import com.matrix.common.enums.PlatformUserTypeEnum;
import com.matrix.common.enums.RoleEnum;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.model.RoleDTO;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.util.SpringUtils;
import com.matrix.common.util.StreamUtils;
import com.matrix.common.util.StringUtils;
import com.matrix.datapermission.enums.DataScopeType;
import com.matrix.datapermission.enums.RoleDataScoop;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;
import java.util.function.Function;

/**
 * 基于角色的数据权限规则
 * <p>
 * 1、管理员及店东支持查看所有数据
 * 2、店长可查看当前所选门店或分店的所有数据
 * 3、经纪人仅可查看自己的数据
 * 4、其他角色不进行处理
 * <p>
 * 详细{@link com.matrix.datapermission.enums.RoleDataScoop}
 *
 * @author ZhaoWeiLong
 * @since 2023/8/28
 **/
@Slf4j
public class RoleDataPermissionRule implements DataPermissionRule {

    /**
     * 表名
     */
    public final Set<String> TABLE_NAMES = new HashSet<>();


    /**
     * 表权限对应字段
     */
    public final HashBasedTable<String, DataScopeType, String> COLUMNS = HashBasedTable.create();

    /**
     * spel 解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();
    /**
     * 解析器上下文
     */
    private final ParserContext parserContext = new TemplateParserContext();
    /**
     * bean解析器 用于处理 spel 表达式中对 bean 的调用
     */
    private final BeanResolver beanResolver = new BeanFactoryResolver(SpringUtils.getBeanFactory());

    public void addTable(Class<? extends BaseEntity> entityClass) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        TABLE_NAMES.add(tableName);
    }

    public void addTable(String tableName) {
        TABLE_NAMES.add(tableName);
    }

    public void addColumn(Class<? extends BaseEntity> entityClass, DataScopeType dataScopeType, String column) {
        String tableName = TableInfoHelper.getTableInfo(entityClass).getTableName();
        COLUMNS.put(tableName, dataScopeType, column);
        addTable(tableName);
    }

    public void addColumn(String tableName, DataScopeType dataScopeType, String column) {
        COLUMNS.put(tableName, dataScopeType, column);
        addTable(tableName);
    }

    @Override
    public Set<String> getTableNames() {
        return TABLE_NAMES;
    }

    @Override
    public Expression getExpression(String tableName, Alias tableAlias) {
        final LoginUser loginUser = LoginUserContextHolder.getUser();
        // 只有用户登陆的情况下，才进行数据权限的处理
        if (loginUser == null) {
            log.debug("loginUser is null ignore data permission");
            return null;
        }
        //只有用户类型为B端用户类型的进行数据处理
        final PlatformUserTypeEnum userType = TerminalContextHolder.getUserType();
        if (ObjectUtils.notEqual(userType, PlatformUserTypeEnum.SYS_USER)) {
            log.debug("userType is not sys_user ignore data permission");
            return null;
        }
        //查询用户角色 无角色不处理 只有B端小程序自助注册的情况无角色 (就算是B端无角色 有租户校验+接口鉴权的情况下也无法查询到数据 所以直接放行
        final List<RoleDTO> roles = loginUser.getRoles();
        if (CollUtil.isEmpty(roles)) {
            log.debug("role is empty ignore data permission");
            return null;
        }
        //是否是管理员或店东 则不进行处理
        final Optional<RoleDTO> optional = StreamEx.of(roles)
                .findFirst(roleDTO -> Objects.equals(RoleEnum.ADMIN.getRoleKey(), roleDTO.getRoleKey())
                        || Objects.equals(RoleEnum.SHOP_OWNER.getRoleKey(), roleDTO.getRoleKey()));
        if (optional.isPresent()) {
            log.debug("role is admin or shop_owner ignore data permission");
            return null;
        }
        String tableAliasName = tableName;
        if (tableAlias != null) {
            tableAliasName = tableAlias.getName();
        }
        String expressionSql = buildExpression(tableName, tableAliasName, roles, loginUser);
        log.debug("buildExpression:{}", expressionSql);
        if (StringUtils.isBlank(expressionSql)) {
            return null;
        }
        try {
            final Expression expression = CCJSqlParserUtil.parseExpression(expressionSql);
            return new Parenthesis(expression);
        } catch (JSQLParserException e) {
            throw new ServiceException(SystemErrorTypeEnum.DATA_PERMISSION_ERROR);
        }
    }

    private String buildExpression(String tableName, String tableAliasName, List<RoleDTO> roles, LoginUser loginUser) {
        String joinStr = " OR ";
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(beanResolver);
        context.setVariable("user", loginUser);
        context.setVariable("tableName", tableAliasName);
        Set<String> conditions = new HashSet<>();
        for (RoleDTO role : roles) {
            final RoleDataScoop roleDataScoop = RoleDataScoop.ofRoleKey(role.getRoleKey());
            if (roleDataScoop == null) {
                return "";
            }
            final DataScopeType dataScope = roleDataScoop.getDataScope();
            final String sqlTemplate = dataScope.getSqlTemplate();
            if (StringUtils.isBlank(sqlTemplate)) {
                return "";
            }
            final String column = COLUMNS.get(tableName, dataScope);
            if (StringUtils.isNotBlank(column)) {
                context.setVariable("column", column);
            } else {
                context.setVariable("column", dataScope.getColumn());
            }

            // 解析sql模板并填充
            String sql = parser.parseExpression(sqlTemplate, parserContext).getValue(context, String.class);
            conditions.add(joinStr + sql);
        }

        if (CollUtil.isNotEmpty(conditions)) {
            String sql = StreamUtils.join(conditions, Function.identity(), "");
            return sql.substring(joinStr.length());
        }
        return "";
    }
}
