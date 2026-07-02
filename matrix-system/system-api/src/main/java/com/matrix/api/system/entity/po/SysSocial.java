package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 社交登录绑定实体 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_social")
@Schema(description = "社交登录绑定")
public class SysSocial extends BaseEntity {
    private Long userId;
    private String authId;
    private String source;
    private String openId;
    private String accessToken;
    private Integer expireIn;
    private String refreshToken;
    private String nickName;
    private String email;
    private String avatar;
    private String unionId;
    private String scope;
}
