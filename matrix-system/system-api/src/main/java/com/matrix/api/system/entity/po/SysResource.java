package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.matrix.common.entity.TenantEntity;
import lombok.Data;

/**
 * (SysResource)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Data
public class SysResource extends TenantEntity {
    @TableId
    private Long id;

    private String name;

    private String url;

    private String description;

    private Long categoryId;


}

