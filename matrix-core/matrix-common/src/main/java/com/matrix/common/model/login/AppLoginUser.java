package com.matrix.common.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * app登录信息
 *
 * @author ZhaoWeiLong
 * @since 2023/7/3
 **/
@Data
@Schema(description = "app登录信息")
public class AppLoginUser extends LoginUser {


    /**
     * 是否已绑定账号
     */
    @Schema(description = "是否已绑定账号")
    private Boolean isBinding = false;

}
