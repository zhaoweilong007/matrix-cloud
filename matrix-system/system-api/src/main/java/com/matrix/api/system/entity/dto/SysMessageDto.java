package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 站内消息 DTO */
@Data
@Schema(description = "消息DTO")
public class SysMessageDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer messageType;
    private Integer status;
    private String jumpUrl;
    private String remark;
}
