package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志 DTO。
 */
@Data
@Schema(description = "操作日志DTO")
public class SysOperateLogDto {

    private Long id;

    @NotBlank(message = "操作人不能为空")
    @Schema(description = "操作人用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "操作人用户ID")
    private Long userId;

    @Schema(description = "部门ID")
    private Long deptId;

    @NotBlank(message = "模块名不能为空")
    @Schema(description = "模块名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String module;

    @Schema(description = "操作名称")
    private String name;

    @Schema(description = "操作类型")
    private Integer type;

    @Schema(description = "请求方法")
    private String requestMethod;

    @Schema(description = "请求URL")
    private String requestUrl;

    @Schema(description = "请求参数")
    private String requestParams;

    @Schema(description = "请求结果")
    private String requestResult;

    @Schema(description = "耗时（毫秒）")
    private Long costTime;

    @Schema(description = "操作IP")
    private String ip;

    @Schema(description = "浏览器UserAgent")
    private String userAgent;

    @Schema(description = "浏览器名称")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "客户端标识")
    private String clientKey;

    @Schema(description = "状态：0-失败 1-成功")
    private Integer status;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "操作时间")
    private LocalDateTime operateTime;
}
