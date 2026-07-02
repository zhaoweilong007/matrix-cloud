package com.matrix.api.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.matrix.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 站内消息实体 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message")
@Schema(description = "站内消息")
public class SysMessage extends BaseEntity {
    private Long userId;
    private String title;
    private String content;
    /** 消息类型：1-系统 2-通知 */
    private Integer messageType;
    /** 状态：0-未读 1-已读 */
    private Integer status;
    private LocalDateTime readTime;
    private String jumpUrl;
    private String remark;
}
