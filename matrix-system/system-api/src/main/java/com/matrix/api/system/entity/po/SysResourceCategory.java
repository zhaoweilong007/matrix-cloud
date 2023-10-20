package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.matrix.entity.po.TenantPo;
import lombok.Data;

/**
 * (SysResourceCategory)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Data
public class SysResourceCategory extends TenantPo<SysResourceCategory> {
    @TableId
    private Long id;

    private String name;

    private Integer sort;


}

