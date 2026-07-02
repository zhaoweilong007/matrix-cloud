package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 站内消息 DTO */
@Data
@Schema(description = "消息DTO")
public class SysMessageDto {
    private Long id;

    @NotNull(message = "接收用户ID不能为空")
    private Long userId;

    @NotBlank(message = "消息标题不能为空")
    private String title;
    private String content;
    private Integer messageType;
    private Integer status;
    private String jumpUrl;
    private String remark;
}
