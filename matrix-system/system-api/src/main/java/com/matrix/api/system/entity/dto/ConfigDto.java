package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 系统配置创建/修改 DTO */
@Data
@Schema(description = "配置DTO")
public class ConfigDto {
    private Long id;

    @NotBlank(message = "参数名称不能为空")
    private String name;

    @NotBlank(message = "参数键名不能为空")
    @Schema(description = "参数键名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String configKey;
    private String value;
    private Integer type;
    private Integer visible;
    private String category;
    private String remark;
}
