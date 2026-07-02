package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.matrix.common.entity.TenantEntity;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (SysRoleResourceRelation)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:52
 */
@Data
@TableName("sys_role_resource_relation")
public class SysRoleResourceRelation extends TenantEntity {

    @TableId
    private Long id;

    private Long roleId;

    private Long resourceId;


}

