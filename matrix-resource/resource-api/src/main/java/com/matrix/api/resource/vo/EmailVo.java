package com.matrix.api.resource.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "邮件vo")
@Data
public class EmailVo {

    /**
     * 收件人
     */
    @Schema(description = "收件人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "收件人(to)不能为空")
    private String to;

    /**
     * 主题
     */
    @Schema(description = "主题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "主题(subject)不能为空")
    private String subject;

    /**
     * 内容
     */
    @Schema(description = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容(content)不能为空")
    private String content;

}
