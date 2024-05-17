package com.matrix.api.resource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Schema(description = "极光推送vo")
@Data
public class JPushVo {

    @Schema(description = "短信内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "短信内容(msg)不能为空")
    private String msg;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题(title)不能为空")
    private String title;

    @Schema(description = "平台", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "平台(platform)不能为空")
    private String platform;

    @Schema(description = "别名")
    private String alias;

    @Schema(description = "设备编号")
    private String deviceNo;

    private Map<String, String> param;
}
