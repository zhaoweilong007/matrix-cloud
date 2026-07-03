package com.matrix.system.config;

import com.matrix.api.system.entity.po.*;
import com.matrix.datapermission.config.PermissionRuleCustomizer;
import com.matrix.datapermission.enums.DataScopeType;
import com.matrix.datapermission.rule.RoleDataPermissionRule;
import org.springframework.stereotype.Component;

/**
 * 系统模块数据权限规则定制。
 *
 * <p>注册需要数据权限过滤的表及其对应字段映射。
 * 基于 SysRole.dataScope 的值（1-全部/2-自定义/3-本部门/4-本部门及以下/5-仅本人）进行过滤。</p>
 */
@Component
public class SystemPermissionRuleCustomizer implements PermissionRuleCustomizer<RoleDataPermissionRule> {

    @Override
    public void customize(RoleDataPermissionRule rule) {
        // 部门作用域：注册需要按部门隔离的表
        rule.addColumn("sys_admin", DataScopeType.SHOP, "dept_id");
        rule.addColumn("sys_dept", DataScopeType.SHOP, "id");
        rule.addColumn("sys_notice", DataScopeType.SHOP, "dept_id");

        // 本人作用域：注册需要按创建人隔离的表
        rule.addColumn("sys_admin", DataScopeType.SELF, "create_by");
        rule.addColumn("sys_notice", DataScopeType.SELF, "create_by");
        rule.addColumn("sys_operate_log", DataScopeType.SELF, "user_id");
    }
}
