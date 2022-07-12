package com.matrix.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/11 15:58
 **/
@Data
public class SysAdminRegisterDto {

    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;


}
