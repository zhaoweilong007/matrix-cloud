package com.matrix.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * (SysAdmin)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
@Data
public class SysAdmin extends BasePo<SysAdmin> {

    @TableId
    private Long id;

    private String username;

    private String password;

    private String icon;

    private String email;

    private String nickName;

    private String note;

    private Date loginTime;

    private Integer status;


}

