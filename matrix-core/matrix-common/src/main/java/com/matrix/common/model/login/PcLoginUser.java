package com.matrix.common.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Data;

/**
 * pc后台登录
 *
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
