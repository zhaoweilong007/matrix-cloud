package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.matrix.entity.po.TenantPo;
import lombok.Data;

/**
 * (SysRoleMenuRelation)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:52
 */
@Data
public class SysRoleMenuRelation extends TenantPo<SysRoleMenuRelation> {

    @TableId
    private Long id;

    private Long roleId;

    private Long menuId;


}

