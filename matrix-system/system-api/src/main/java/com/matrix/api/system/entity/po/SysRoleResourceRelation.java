package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.matrix.entity.po.TenantPo;
import lombok.Data;

/**
 * (SysRoleResourceRelation)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:52
 */
@Data
public class SysRoleResourceRelation extends TenantPo<SysRoleResourceRelation> {

    @TableId
    private Long id;

    private Long roleId;

    private Long resourceId;


}

