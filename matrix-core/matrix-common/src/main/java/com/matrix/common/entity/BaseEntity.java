package com.matrix.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 描述：<p>实体类基类 mybatis plus通用</p>
 *
 * @author ZhaoWeiLong
 * @since 2023/2/27
 **/
@Schema(name = "BaseEntity", description = "基础实体")
@Data
public abstract class BaseEntity extends BaseIdEntity {

    public static final String CREATE_BY = "createdBy";
    public static final String UPDATE_BY = "updatedBy";
    /**
     * 创建人
     */
    @Schema(description = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    /**
     * 修改人
     */
    @Schema(description = "修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    /**
     * 是否删除 1：已删除 0：未删除
     */
    @Schema(description = "是否删除1：已删除0：未删除")
    @TableLogic
    @JsonIgnore
    private Boolean deleted;

}
