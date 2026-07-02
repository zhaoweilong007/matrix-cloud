package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 角色-部门关联实体（数据权限自定义部门范围） */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_dept")
@Schema(description = "角色部门关联")
public class SysRoleDept extends BaseEntity {
    private Long roleId;
    private Long deptId;
}
