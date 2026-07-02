package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 社交账号绑定 DTO */
@Data
@Schema(description = "社交绑定DTO")
public class SocialBindDto {
    private Long userId;
    private String source;
    private String authCode;
    private String state;
}
