package com.matrix.datapermission.config;

import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.matrix.datapermission.aop.DataPermissionAnnotationAdvisor;
import com.matrix.datapermission.db.DataPermissionDatabaseInterceptor;
import com.matrix.datapermission.rule.*;
import com.matrix.mybatis.utils.MyBatisUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 数据权限的自动配置类
 *
 * @author ZhaoWeiLong
 * @since 2023/8/25
 **/
@AutoConfiguration
public class DataPermissionConfig {


    /**
     * 数据权限规则工厂
     *
     * @param rules 规则
     * @return {@link DataPermissionRuleFactory}
     */
    @Bean
    public DataPermissionRuleFactory dataPermissionRuleFactory(List<DataPermissionRule> rules) {
        return new DataPermissionRuleFactoryImpl(rules);
    }

    /**
     * 数据库权限数据拦截器
     *
     * @param interceptor 拦截器
     * @param ruleFactory 规则工厂
     * @return {@link DataPermissionDatabaseInterceptor}
     */
    @Bean
    public DataPermissionDatabaseInterceptor dataPermissionDatabaseInterceptor(MybatisPlusInterceptor interceptor,
                                                                               DataPermissionRuleFactory ruleFactory) {
        // 创建 DataPermissionDatabaseInterceptor 拦截器
        DataPermissionDatabaseInterceptor inner = new DataPermissionDatabaseInterceptor(ruleFactory);
        // 添加到 interceptor 中
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return inner;
    }

    /**
     * 数据权限切面
     *
     * @return {@link DataPermissionAnnotationAdvisor}
     */
    @Bean
    public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
        return new DataPermissionAnnotationAdvisor();
    }

    /**
     * 门店数据权限规则
     *
     * @param customizers 定制
     * @return {@link ShopDataPermissionRule}
     */
    @Bean
    public ShopDataPermissionRule shopDataPermissionRule(List<PermissionRuleCustomizer> customizers) {
        final ShopDataPermissionRule shopDataPermissionRule = new ShopDataPermissionRule();
        setPermissionRuleCustomizer(shopDataPermissionRule, customizers);
        return shopDataPermissionRule;
    }

    /**
     * 用户数据权限规则
     *
     * @param customizers 定制
     * @return {@link UserDataPermissionRule}
     */
    @Bean
    public UserDataPermissionRule userDataPermissionRule(List<PermissionRuleCustomizer> customizers) {
        final UserDataPermissionRule userDataPermissionRule = new UserDataPermissionRule();
        setPermissionRuleCustomizer(userDataPermissionRule, customizers);
        return userDataPermissionRule;
    }


    /**
     * 角色数据权限规则
     *
     * @param customizers 定制
     * @return {@link RoleDataPermissionRule}
     */
    @Bean
    public RoleDataPermissionRule roleDataPermissionRule(List<PermissionRuleCustomizer> customizers) {
        final RoleDataPermissionRule dataPermissionRule = new RoleDataPermissionRule();
        setPermissionRuleCustomizer(dataPermissionRule, customizers);
        return dataPermissionRule;
    }


    /**
     * 设置自定义权限规则
     *
     * @param rule        规则
     * @param customizers 定制
     */
    private void setPermissionRuleCustomizer(DataPermissionRule rule, List<PermissionRuleCustomizer> customizers) {
        customizers.forEach(permissionRuleCustomizer -> {
            final Class<?> ruleClass = ClassUtil.getTypeArgument(permissionRuleCustomizer.getClass(), 0);
            if (ruleClass.isAssignableFrom(rule.getClass())) {
                permissionRuleCustomizer.customize(rule);
            }
        });
    }

}
