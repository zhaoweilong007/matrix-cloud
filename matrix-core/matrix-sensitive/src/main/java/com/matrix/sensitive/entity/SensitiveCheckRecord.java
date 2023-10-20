package com.matrix.sensitive.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测记录
 *
 * @author ZhaoWeiLong
 * @since 2023/4/20
 **/
@Schema(description = "检测记录*/")
@Accessors(chain = true)
@Data
public class SensitiveCheckRecord implements Serializable {

    /**
     * 数据唯一标识
     */
    @Schema(description = "数据唯一标识")
    private Long dataId;

    /**
     * 全局id
     */
    @Schema(description = "全局id")
    private Long globalId;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private Integer type;

    /**
     * 检测任务ID
     */
    @Schema(description = "检测任务ID")
    private String taskId;

    /**
     * 关联id
     */
    @Schema(description = "关联id")
    private Long relateId;

    /**
     * 关联class名称
     */
    @Schema(description = "关联class名称")
    private String className;

    /**
     * 字段名称
     */
    @Schema(description = "字段名称")
    private String fieldName;

    /**
     * -1 检测中 0：通过，2：不通过
     */
    @Schema(description = "-1 检测中 0：通过，2：不通过")
    private Integer suggestion;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private Long censorTime;

    /**
     * 命中的关键字
     */
    @Schema(description = "命中的关键字")
    private List<String> keywords;

    /**
     * 命中的标签分类
     */
    @Schema(description = "命中的标签分类")
    private List<String> labels;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 回调结果
     */
    @Schema(description = "回调结果")
    private String result;
}
