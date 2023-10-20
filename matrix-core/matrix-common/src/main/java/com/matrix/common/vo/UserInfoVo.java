package com.matrix.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhaoWeiLong
 * @since 2023/7/31
 **/
@Data
@Schema(description = "用户信息")
public class UserInfoVo implements Serializable {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "机构名称")
    private String merchantName;

    @Schema(description = "门店名称")
    private String shopName;

    @Schema(description = "分店名称")
    private String branchName;


}
