package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 通知创建/修改 DTO */
@Data
@Schema(description = "通知DTO")
public class NoticeDto {
    private Long id;
    @NotBlank(message = "通知标题不能为空")
    private String title;
    @NotBlank(message = "通知内容不能为空")
    private String content;
    private Integer type;
    private Integer status;
    private String remark;
}
