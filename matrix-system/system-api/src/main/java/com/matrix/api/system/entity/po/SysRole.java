package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.matrix.common.entity.TenantEntity;
import lombok.Data;

/**
 * (SysRole)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:51
 */
@Data
public class SysRole extends TenantEntity {
    @TableId
    private Long id;

    private String name;

    /** 角色编码（唯一），如 super_admin */
    private String code;

    /** 角色类型：1-内置 2-自定义 */
    private Integer type;

    /** 数据范围：1-全部 2-自定义 3-本部门 4-本部门及以下 5-仅本人 */
    private Integer dataScope;

    private String description;

    private Integer adminCount;

    private Integer status;

    private Integer sort;

    /** 备注 */
    private String remark;


}

