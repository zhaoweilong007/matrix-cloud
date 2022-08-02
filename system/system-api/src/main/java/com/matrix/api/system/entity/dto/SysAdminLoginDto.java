package com.matrix.api.system.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/11 15:58
 **/
@Data
public class SysAdminLoginDto {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotBlank(message = "验证码唯一标识不能为空")
    private String uuid;
}
