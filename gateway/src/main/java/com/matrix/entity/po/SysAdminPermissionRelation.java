package com.matrix.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (SysAdminPermissionRelation)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
@Data
public class SysAdminPermissionRelation extends Model<SysAdminPermissionRelation> {
    @TableId
    private Long id;

    private Long adminId;

    private Long permissionId;

    private Integer type;



}

