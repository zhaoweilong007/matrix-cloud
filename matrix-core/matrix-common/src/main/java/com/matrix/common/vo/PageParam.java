package com.matrix.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：<p></p>
 *
 * @author ZhaoWeiLong
 * @since 2023/3/3
 **/
@Schema(name = "分页参数", description = "分页参数")
@Data
public class PageParam implements Serializable {


    public static final Integer PAGE_NUM = 1;
    public static final Integer PAGE_SIZE = 10;
    @Schema(hidden = true)
    private final Boolean deleted = false;
    /**
     * 搜索值
     */
    @Schema(description = "搜索值")
    private String searchValue;
    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNum = PAGE_NUM;
    /**
     * 每页数量
     */
    @Schema(description = "每页数量")
    @Min(value = 1, message = "每页数量最小值为 1")
    @Max(value = 100, message = "每页数量最大值为 100")
    private Integer pageSize = PAGE_SIZE;
    /**
     * 排序字段
     */
    @Schema(description = "排序字段 支持多字段如：{orderBy:\"asc,desc\",orderField:\"id,createTime\"}")
    private String sort;
    /**
     * 升序、降序
     */
    @Schema(description = "升序、降序/asc、desc")
    private String order;
}
