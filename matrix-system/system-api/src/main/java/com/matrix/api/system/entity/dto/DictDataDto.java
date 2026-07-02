package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 字典数据创建/修改 DTO */
@Data
@Schema(description = "字典数据DTO")
public class DictDataDto {
    private Long id;
    @Schema(description = "字典类型编码")
    private String dictType;
    private String label;
    private String value;
    private Integer sort;
    private Integer status;
    private String colorType;
    private String cssClass;
    private String remark;
}
