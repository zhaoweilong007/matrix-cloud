package com.matrix.sensitive.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ZhaoWeiLong
 * @since 2023/5/9
 **/
@Schema(description = "检测关联对象")
@Accessors(chain = true)
@Data
public class SensitiveRelate implements Serializable {


    /**
     * 全局唯一id
     */
    @Schema(description = "全局唯一id")
    private Long globalId;

    /**
     * 关联id
     */
    @Schema(description = "关联id")
    private Long relateId;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private Integer totalRecord;

    /**
     * 是否已推送
     */
    @Schema(description = "是否已推送")
    private Boolean pushed;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
