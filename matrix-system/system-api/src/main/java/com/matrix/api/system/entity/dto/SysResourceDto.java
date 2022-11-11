package com.matrix.api.system.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/14 16:02
 **/
@Data
public class SysResourceDto {

    private Long id;

    @NotBlank(message = "资源名称不能为空")
    private String name;

    @NotBlank(message = "资源路径不能为空")
    private String url;

    private String description;

    @NotNull
    private Long categoryId;

}
