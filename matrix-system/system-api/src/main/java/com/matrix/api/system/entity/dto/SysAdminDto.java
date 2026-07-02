package com.matrix.api.system.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 系统用户创建/修改 DTO。
 *
 * <p>不包含密码字段，密码通过 {@link UpdateAdminPasswordDto} 单独设置。</p>
 */
@Data
@Schema(description = "系统用户DTO")
public class SysAdminDto {

    private Long id;

    @NotBlank(message = "用户名称不能为空")
    @Schema(description = "用户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "关联部门ID")
    private Long deptId;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "性别：0-未知 1-男 2-女")
    private Integer sex;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;

    @Schema(description = "用户类型")
    private String userType;
}
