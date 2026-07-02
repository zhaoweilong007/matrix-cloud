package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统配置实体。
 *
 * <p>内置参数（{@code type=1}）不可删除。</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
@Schema(description = "系统配置")
public class SysConfig extends TenantEntity {

    /** 参数名称 */
    private String name;

    /** 参数键名（唯一） */
    private String configKey;

    /** 参数值 */
    private String value;

    /** 是否系统内置：1-是 0-否 */
    private Integer type;

    /** 是否可见：1-是 0-否 */
    private Integer visible;

    /** 备注 */
    private String remark;
}
