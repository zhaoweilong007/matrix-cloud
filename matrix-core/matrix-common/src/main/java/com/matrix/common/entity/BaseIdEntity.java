package com.matrix.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.dromara.core.trans.vo.TransPojo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * ID 主键基类
 */
@Data
public abstract class BaseIdEntity implements TransPojo, Serializable {

    /**
     * 数据库主键 ID 列名
     */
    public static final String COL_ID = "id";
    /**
     * 雪花id
     */
    @Schema(description = "主键id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
}
