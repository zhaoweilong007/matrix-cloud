package com.matrix.entity.dto;

import lombok.Data;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/12 11:31
 **/
@Data
public class UpdateAdminPasswordDto {

    private Long id;
    private String oldPassword;
    private String newPassword;
}
