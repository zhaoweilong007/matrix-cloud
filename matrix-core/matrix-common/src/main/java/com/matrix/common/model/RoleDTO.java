package com.matrix.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色
 */

@Schema(description = "角色")
@Data
@NoArgsConstructor
public class RoleDTO implements Serializable {


    @Schema(description = "用户id")
    private Long userId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色标识
     */
    @Schema(description = "角色标识")
    private String roleKey;

}
