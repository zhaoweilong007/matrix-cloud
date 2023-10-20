package com.matrix.common.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageMultParam<T> extends Page<T> {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNum = 1;

    /**
     * 每页数量
     */
    @Schema(description = "每页数量")
    @Min(value = 1, message = "每页数量最小值为 1")
    @Max(value = 100, message = "每页数量最大值为 100")
    private Integer pageSize = 10;

    private T data;

    @Override
    public long getSize() {
        return this.getPageSize();
    }

    @Override
    public long getCurrent() {
        return this.getPageNum();
    }
}
