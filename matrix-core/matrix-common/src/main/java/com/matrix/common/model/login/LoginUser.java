package com.matrix.common.model.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.matrix.common.constant.CacheConstants;
import com.matrix.common.model.RoleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;

/**
 * 用户信息
 */
@Schema(description = "用户信息")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginUser implements Serializable, Principal {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 租户id
     */
    @Schema(description = "租户id")
    private Long tenantId;

    /**
     * 门店id
     */
    @Schema(description = "门店Id")
    private Long shopId;

    @Schema(description = "分店id")
    private Long branchId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String mobile;

    /**
     * 密码
     */
    @Schema(hidden = true)
    @JsonIgnore
    private String password;

    /**
     * 用户唯一标识
     */
    @Schema(description = "用户唯一标识")
    private String token;

    /**
     * 用户类型
     */
    @Schema(description = "设备类型")
    private String deviceType;

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<RoleDTO> roles;

    public static String getLoginId(String deviceType, Long userId) {
        if (deviceType == null) {
            throw new IllegalArgumentException("设备类型不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return deviceType + CacheConstants.LOGINID_JOIN_CODE + userId;
    }

    /**
     * 获取登录id
     */
    public String obtainedLoginId() {
        if (deviceType == null) {
            throw new IllegalArgumentException("用户类型不能为空");
        }
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return deviceType + CacheConstants.LOGINID_JOIN_CODE + userId;
    }

    @Override
    public String getName() {
        if (userId == null) {
            return null;
        }
        return this.userId.toString();
    }
}
