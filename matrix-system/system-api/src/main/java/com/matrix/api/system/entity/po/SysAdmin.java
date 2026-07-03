package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.matrix.common.annotation.Sensitive;
import com.matrix.common.enums.SensitiveStrategyEnum;
import com.matrix.common.entity.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * (SysAdmin)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
@Data
@Schema(description = "系统用户")
@TableName("sys_admin")
public class SysAdmin extends TenantEntity {

    @TableId
    @Schema(description = "用户ID")
    private Long id;


    @Schema(description = "用户名称")
    private String username;

    @Sensitive(strategy = SensitiveStrategyEnum.PASSWORD)
    private String password;

    private String icon;

    @Sensitive(strategy = SensitiveStrategyEnum.EMAIL)
    private String email;

    private String nickName;

    private String note;

    /** 关联部门 ID */
    private Long deptId;

    /** 手机号 */
    @Sensitive(strategy = SensitiveStrategyEnum.PHONE)
    private String mobile;

    /** 性别：0-未知 1-男 2-女 */
    private Integer sex;

    /** 头像 URL */
    private String avatar;

    /** 备注 */
    private String remark;

    /** 最后登录 IP */
    private String loginIp;

    private Date loginTime;

    private Integer status;

    private String userType;


    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }


}

