package com.matrix.common.model.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信小程序登录
 *
 * @author ZhaoWeiLong
 * @since 2023/7/3
 **/
@Data
@Schema(description = "微信登录信息")
public class WxLoginUser extends LoginUser {

    /**
     * openid
     */
    @Schema(description = "微信openId")
    private String wxOpenid;

}
