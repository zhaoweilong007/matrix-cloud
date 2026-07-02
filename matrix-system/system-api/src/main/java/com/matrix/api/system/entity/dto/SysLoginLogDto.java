package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录日志 DTO。
 */
@Data
@Schema(description = "登录日志DTO")
public class SysLoginLogDto {

    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "登录IP")
    private String ip;

    @Schema(description = "浏览器UserAgent")
    private String userAgent;

    @Schema(description = "登录状态：0-失败 1-成功")
    private Integer status;

    @Schema(description = "登录结果描述")
    private String result;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;
}
