package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门实体。
 *
 * <p>树形结构，通过 {@code parentId} 关联上级部门。</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
@Schema(description = "部门")
public class SysDept extends TenantEntity {

    /** 父部门 ID，0 表示根部门 */
    private Long parentId;

    /** 部门名称 */
    private String name;

    /** 负责人 */
    private String leader;

    /** 负责人用户 ID（关联 sys_admin.id） */
    private Long leaderUserId;

    /** 祖级列表（如 "0,100,101"），高效子树查询 */
    private String ancestors;

    /** 部门类别编码 */
    private String deptCategory;

    /** 联系电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 排序 */
    private Integer sort;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 子部门列表（非数据库字段） */
    @TableField(exist = false)
    private List<SysDept> children;
}
