package com.matrix.datapermission.annotation;

import com.matrix.common.entity.BaseEntity;
import com.matrix.datapermission.rule.DataPermissionRule;
import com.matrix.datapermission.rule.RoleDataPermissionRule;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * 可声明在类或者方法上，标识使用的数据权限规则
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    /**
     * 当前类或方法是否开启数据权限
     * 即使不添加 @DataPermission 注解，默认开启
     * 可通过设置 enable 为 false 禁用
     */
    boolean enable() default true;

    /**
     * 生效的数据权限规则数组，优先级高于 {@link #excludeRules()}
     */
    Class<? extends DataPermissionRule>[] includeRules() default RoleDataPermissionRule.class;

    /**
     * 排除的数据权限规则数组，优先级最低
     */
    Class<? extends DataPermissionRule>[] excludeRules() default {};

    /**
     * 排除指定的表
     */
    Class<? extends BaseEntity>[] excludeTables() default {};

}
