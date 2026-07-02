package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 通知创建/修改 DTO */
@Data
@Schema(description = "通知DTO")
public class NoticeDto {
    private Long id;
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private String remark;
}
