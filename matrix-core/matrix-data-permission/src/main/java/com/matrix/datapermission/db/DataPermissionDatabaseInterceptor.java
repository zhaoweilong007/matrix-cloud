package com.matrix.datapermission.db;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.matrix.common.entity.BaseEntity;
import com.matrix.common.util.collection.SetUtils;
import com.matrix.datapermission.annotation.DataPermission;
import com.matrix.datapermission.aop.DataPermissionContextHolder;
import com.matrix.datapermission.rule.DataPermissionRule;
import com.matrix.datapermission.rule.DataPermissionRuleFactory;
import com.matrix.mybatis.utils.MyBatisUtils;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 数据权限拦截器，通过 {@link com.matrix.datapermission.rule.DataPermissionRule} 数据权限规则，重写 SQL 的方式来实现
 * <p>
 * 整体的代码实现上，参考 {@link com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor} 实现。
 * 所以每次 MyBatis Plus 升级时，需要 Review 下其具体的实现是否有变更！
 */
@RequiredArgsConstructor
public class DataPermissionDatabaseInterceptor extends JsqlParserSupport implements InnerInterceptor {

    /**
     * 数据权限规则工厂
     */
    private final DataPermissionRuleFactory ruleFactory;

    /**
     * MappedStatement 缓存，记录无需重写的 SQL
     */
    @Getter
    private final MappedStatementCache mappedStatementCache = new MappedStatementCache();

    @Override // SELECT 场景
    public void beforeQuery(
            Executor executor,
            MappedStatement ms,
            Object parameter,
            RowBounds rowBounds,
            ResultHandler resultHandler,
            BoundSql boundSql) {
        // 获得 Mapper 对应的数据权限的规则
        List<DataPermissionRule> rules = ruleFactory.getDataPermissionRule(ms.getId());
        if (mappedStatementCache.noRewritable(ms, rules)) { // 如果无需重写，则跳过
            return;
        }

        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        try {
            // 初始化上下文
            ContextHolder.init(rules);
            // 处理 SQL
            mpBs.sql(parserSingle(mpBs.sql(), null));
        } finally {
            // 添加是否需要重写的缓存
            addMappedStatementCache(ms);
            // 清空上下文
            ContextHolder.clear();
        }
    }

    @Override // 只处理 UPDATE / DELETE 场景，不处理 INSERT 场景（因为 INSERT 不需要数据权限)
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            // 获得 Mapper 对应的数据权限的规则
            List<DataPermissionRule> rules = ruleFactory.getDataPermissionRule(ms.getId());
            if (mappedStatementCache.noRewritable(ms, rules)) { // 如果无需重写，则跳过
                return;
            }

            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            try {
                // 初始化上下文
                ContextHolder.init(rules);
                // 处理 SQL
                mpBs.sql(parserMulti(mpBs.sql(), null));
            } finally {
                // 添加是否需要重写的缓存
                addMappedStatementCache(ms);
                // 清空上下文
                ContextHolder.clear();
            }
        }
    }

    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        // TODO: 实现 SELECT 语句的数据权限改写
    }

    /**
     * update 语句处理
     */
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        // TODO: 实现 UPDATE 语句的数据权限改写
    }

    /**
     * delete 语句处理
     */
    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        // TODO: 实现 DELETE 语句的数据权限改写
    }

    /**
     * 构建指定表的数据权限的 Expression 过滤条件
     *
     * @param table 表
     * @return Expression 过滤条件
     */
    private Expression buildDataPermissionExpression(Table table) {
        // 生成条件
        Expression allExpression = null;
        for (DataPermissionRule rule : ContextHolder.getRules()) {
            // 判断表名是否匹配
            if (!rule.getTableNames().contains(table.getName())) {
                continue;
            }

            // 如果有匹配的规则，说明可重写。
            // 为什么不是有 allExpression 非空才重写呢？在生成 column = value 过滤条件时，会因为 value 不存在，导致未重写。
            // 这样导致第一次无 value，被标记成无需重写；但是第二次有 value，此时会需要重写。
            ContextHolder.setRewrite(true);

            String tableName = MyBatisUtils.getTableName(table);

            // 排除指定表
            final DataPermission dataPermission = DataPermissionContextHolder.get();
            final Class<? extends BaseEntity>[] ignoreTables = dataPermission.excludeTables();
            if (ArrayUtil.isNotEmpty(ignoreTables)) {
                final List<String> ignoreTableNames = Arrays.stream(ignoreTables)
                        .map(aClass -> TableInfoHelper.getTableInfo(aClass).getTableName())
                        .filter(Objects::nonNull)
                        .toList();
                if (CollUtil.contains(ignoreTableNames, tableName)) {
                    continue;
                }
            }

            // 单条规则的条件
            Expression oneExpress = rule.getExpression(tableName, table.getAlias());
            if (oneExpress == null) {
                continue;
            }
            // 拼接到 allExpression 中
            allExpression = allExpression == null ? oneExpress : new AndExpression(allExpression, oneExpress);
        }

        return allExpression;
    }

    /**
     * 判断 SQL 是否重写。如果没有重写，则添加到 {@link MappedStatementCache} 中
     *
     * @param ms MappedStatement
     */
    private void addMappedStatementCache(MappedStatement ms) {
        if (ContextHolder.getRewrite()) {
            return;
        }
        // 无重写，进行添加
        mappedStatementCache.addNoRewritable(ms, ContextHolder.getRules());
    }

    /**
     * SQL 解析上下文，方便透传 {@link DataPermissionRule} 规则
     */
    static final class ContextHolder {

        /**
         * 该 {@link MappedStatement} 对应的规则
         */
        private static final ThreadLocal<List<DataPermissionRule>> RULES =
                ThreadLocal.withInitial(Collections::emptyList);
        /**
         * SQL 是否进行重写
         */
        private static final ThreadLocal<Boolean> REWRITE = ThreadLocal.withInitial(() -> Boolean.FALSE);

        public static void init(List<DataPermissionRule> rules) {
            RULES.set(rules);
            REWRITE.set(false);
        }

        public static void clear() {
            RULES.remove();
            REWRITE.remove();
        }

        public static boolean getRewrite() {
            return REWRITE.get();
        }

        public static void setRewrite(boolean rewrite) {
            REWRITE.set(rewrite);
        }

        public static List<DataPermissionRule> getRules() {
            return RULES.get();
        }
    }

    /**
     * {@link MappedStatement} 缓存
     * 目前主要用于，记录 {@link DataPermissionRule} 是否对指定 {@link MappedStatement} 无效
     * 如果无效，则可以避免 SQL 的解析，加快速度
     */
    static final class MappedStatementCache {

        /**
         * 指定数据权限规则，对指定 MappedStatement 无需重写（不生效)的缓存
         * <p>
         * value：{@link MappedStatement#getId()} 编号
         */
        @Getter
        private final Map<Class<? extends DataPermissionRule>, Set<String>> noRewritableMappedStatements =
                new ConcurrentHashMap<>();

        /**
         * 判断是否无需重写
         * ps：虽然有点中文式英语，但是容易读懂即可
         *
         * @param ms    MappedStatement
         * @param rules 数据权限规则数组
         * @return 是否无需重写
         */
        public boolean noRewritable(MappedStatement ms, List<DataPermissionRule> rules) {
            // 如果规则为空，说明无需重写
            if (CollUtil.isEmpty(rules)) {
                return true;
            }
            // 任一规则不在 noRewritableMap 中，则说明可能需要重写
            for (DataPermissionRule rule : rules) {
                Set<String> mappedStatementIds = noRewritableMappedStatements.get(rule.getClass());
                if (!CollUtil.contains(mappedStatementIds, ms.getId())) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 添加无需重写的 MappedStatement
         *
         * @param ms    MappedStatement
         * @param rules 数据权限规则数组
         */
        public void addNoRewritable(MappedStatement ms, List<DataPermissionRule> rules) {
            for (DataPermissionRule rule : rules) {
                Set<String> mappedStatementIds = noRewritableMappedStatements.get(rule.getClass());
                if (CollUtil.isNotEmpty(mappedStatementIds)) {
                    mappedStatementIds.add(ms.getId());
                } else {
                    noRewritableMappedStatements.put(rule.getClass(), SetUtils.asSet(ms.getId()));
                }
            }
        }

        /**
         * 清空缓存
         * 目前主要提供给单元测试
         */
        public void clear() {
            noRewritableMappedStatements.clear();
        }
    }
}
