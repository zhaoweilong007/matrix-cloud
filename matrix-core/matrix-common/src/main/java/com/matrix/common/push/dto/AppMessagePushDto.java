package com.matrix.common.push.dto;

import com.matrix.common.push.enums.AppEnum;
import com.matrix.common.push.enums.PushMessageEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author LeonZhou
 * @since 2023/12/25
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMessagePushDto implements Serializable {


    @Schema(description = "模板")
    private PushMessageEnum messageEnum;

    @Schema(description = "接收用户ids")
    @NotEmpty
    private List<Long> userIds;

    @Schema(description = "极光推送APP类型")
    @NotEmpty
    private AppEnum appEnum;

    @Schema(description = "业务类型")
    @NotEmpty
    private String type;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "参数")
    private Map<String, Object> extras;

}
