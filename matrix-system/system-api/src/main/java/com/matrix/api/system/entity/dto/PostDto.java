package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 岗位创建/修改 DTO */
@Data
@Schema(description = "岗位DTO")
public class PostDto {
    private Long id;
    private String code;
    @Schema(description = "岗位名称")
    private String name;
    private Integer sort;
    private Integer status;
    private String remark;
}
