package com.matrix.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * (SysRoleResourceRelation)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:52
 */
@Data
public class SysRoleResourceRelation extends Model<SysRoleResourceRelation> {

    @TableId
    private Long id;

    private Long roleId;

    private Long resourceId;



}

