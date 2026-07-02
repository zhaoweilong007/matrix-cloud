package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** OAuth2 客户端管理实体 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_client")
@Schema(description = "OAuth2客户端")
public class SysClient extends BaseEntity {
    private String clientId;
    private String clientKey;
    private String clientSecret;
    private String grantTypes;
    private String deviceType;
    private Long activeTimeout;
    private Long timeout;
    private Integer status;
    private String remark;
}
