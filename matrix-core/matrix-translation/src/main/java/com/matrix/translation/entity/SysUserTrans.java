package com.matrix.translation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.core.trans.vo.TransPojo;
import com.matrix.common.entity.BaseIdEntity;
import lombok.Data;

/**
 * 用户翻译实体，用于 easy-trans 远程翻译缓存
 */
@Data
@TableName("sys_user")
public class SysUserTrans extends BaseIdEntity implements TransPojo {

    /**
     * easy-trans 远程翻译缓存类全名
     */
    public static final String CLASS_NAME = "com.matrix.translation.entity.SysUserTrans";

    /**
     * 用户名称
     */
    private String userName;
}
