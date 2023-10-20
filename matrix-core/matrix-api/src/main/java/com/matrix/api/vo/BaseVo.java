package com.matrix.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通用Vo
 **/
@Data
public class BaseVo implements Serializable {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "多租户编号")
    private Long tenantId;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "创建人id")
    private Long createdBy;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "修改人id")
    private Long updatedBy;


}
