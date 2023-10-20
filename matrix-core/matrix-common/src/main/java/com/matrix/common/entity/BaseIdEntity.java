package com.matrix.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fhs.core.trans.vo.TransPojo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/3
 **/
@Data
public abstract class BaseIdEntity implements TransPojo, Serializable {

    public static final String COL_ID = "id";
    /**
     * 雪花id
     */
    @Schema(description = "主键id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
}
