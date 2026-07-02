package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 字典类型创建/修改 DTO */
@Data
@Schema(description = "字典类型DTO")
public class DictTypeDto {
    private Long id;
    private String name;
    @Schema(description = "字典类型编码")
    private String type;
    private Integer status;
    private String remark;
}
