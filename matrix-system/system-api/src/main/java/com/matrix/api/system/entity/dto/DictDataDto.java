package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** 字典数据创建/修改 DTO */
@Data
@Schema(description = "字典数据DTO")
public class DictDataDto {
    private Long id;
    @Schema(description = "字典类型编码")
    @NotBlank(message = "字典类型编码不能为空")
    private String dictType;
    @NotBlank(message = "字典标签不能为空")
    private String label;
    @NotBlank(message = "字典值不能为空")
    private String value;
    private Integer sort;
    private Integer status;
    private String colorType;
    private String cssClass;
    private String remark;
}
