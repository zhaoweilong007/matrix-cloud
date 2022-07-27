package com.matrix.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述：登录用户
 *
 * @author zwl
 * @since 2022/7/26 14:42
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {

    private Long userId;
    private String userType;

    private String username;

    private Long tenantId;


    public String getLoginId() {
        return userType + ":" + userId;
    }


}
