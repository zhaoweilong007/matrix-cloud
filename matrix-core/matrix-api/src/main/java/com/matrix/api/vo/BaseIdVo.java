package com.matrix.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
