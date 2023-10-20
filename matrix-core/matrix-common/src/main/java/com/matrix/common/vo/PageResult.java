package com.matrix.common.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 分页对象
 *
 * @param <E> 数据list
 */
@Schema(description = "分页对象")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<E> {

    /**
     * 当前页
     */
    @Schema(description = "当前页")
    private int pageNum;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量")
    private int pageSize;

    /**
     * 数据总量
     */
    @Schema(description = "数据总量")
    private int total;

    /**
     * 数据
     */
    @Schema(description = "数据")
    private List<E> data;


    public static <E> PageResult<E> emptyResult(PageParam param) {
        return PageResult.<E>builder()
                .pageNum(param.getPageNum())
                .pageSize(param.getPageSize())
                .total(0)
                .data(Collections.emptyList())
                .build();
    }
}
