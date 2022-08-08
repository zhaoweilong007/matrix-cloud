package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.matrix.annotation.Sensitive;
import com.matrix.entity.enums.SensitiveStrategy;
import com.matrix.entity.po.TenantPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * (SysAdmin)表实体类
 *
 * @author zhaoweilong
 * @since 2022-07-11 16:53:49
 */
@Data
@ApiModel(value = "系统用户")
public class SysAdmin extends TenantPo<SysAdmin> {

    @TableId
    @ApiModelProperty(value = "用户ID")
    private Long id;


    @ApiModelProperty(value = "用户名称")
    private String username;

    @Sensitive(strategy = SensitiveStrategy.PASSWORD)
    private String password;

    private String icon;

    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    private String email;

    private String nickName;

    private String note;

    private Date loginTime;

    private Integer status;

    private String userType;


    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }


}

