package com.matrix.translation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fhs.core.trans.vo.TransPojo;
import com.matrix.common.entity.BaseIdEntity;
import lombok.Data;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/31
 **/
@Data
@TableName("sys_user")
public class SysUserTrans extends BaseIdEntity implements TransPojo {

    public static final String CLASS_NAME = "com.matrix.translation.entity.SysUserTrans";

    private String userName;
}
