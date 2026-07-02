package com.matrix.api.system.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 通用简化 VO（下拉框用） */
@Data
@Schema(description = "简化VO")
public class SimpleVo {
    private Long id;
    private String name;
}
