package com.matrix.api.system.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/** 部门树形 VO */
@Data
@Schema(description = "部门树")
public class DeptTreeVo {
    private Long id;
    private Long parentId;
    private String name;
    private Integer sort;
    private Integer status;
    private List<DeptTreeVo> children;
}
