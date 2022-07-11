package com.matrix.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (SysAdminRoleRelation)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:50
 */
@Data
public class SysAdminRoleRelation extends Model<SysAdminRoleRelation> {
    @TableId
    private Long id;

    private Long adminId;

    private Long roleId;



}

