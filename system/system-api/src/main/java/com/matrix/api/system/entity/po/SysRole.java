package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.matrix.entity.po.TenantPo;
import lombok.Data;

/**
 * (SysRole)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Data
public class SysRole extends TenantPo<SysRole> {
    @TableId
    private Long id;

    private String name;

    private String description;

    private Integer adminCount;

    private Integer status;

    private Integer sort;


}

