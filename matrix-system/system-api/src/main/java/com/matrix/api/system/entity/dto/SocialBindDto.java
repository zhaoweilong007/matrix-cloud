package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 社交账号绑定 DTO */
@Data
@Schema(description = "社交绑定DTO")
public class SocialBindDto {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "社交平台不能为空")
    private String source;
    private String authCode;
    private String state;
}
