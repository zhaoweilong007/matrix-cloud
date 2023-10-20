package com.matrix.common.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * pc后台登录
 *
 * @author ZhaoWeiLong
 * @since 2023/7/3
 **/
@Schema(description = "pc后台登录")
@Data
public class PcLoginUser extends LoginUser {


    /**
     * 菜单权限
     */
    @Schema(hidden = true)
    private Set<String> menuPermission;

    /**
     * 角色权限
     */
    @Schema(hidden = true)
    private Set<String> rolePermission;
}
