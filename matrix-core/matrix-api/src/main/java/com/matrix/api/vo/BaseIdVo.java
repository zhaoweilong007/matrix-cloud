package com.matrix.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用Vo
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseIdVo implements Serializable {

    @Schema(description = "主键id")
    private Long id;
}
