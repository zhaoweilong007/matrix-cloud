package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告实体。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
@Schema(description = "通知公告")
public class SysNotice extends TenantEntity {

    /** 通知标题 */
    private String title;

    /** 通知内容 */
    private String content;

    /** 通知类型：1-系统通知 2-业务通知 */
    private Integer type;

    /** 状态：0-草稿 1-已发布 */
    private Integer status;

    /** 备注 */
    private String remark;
}
