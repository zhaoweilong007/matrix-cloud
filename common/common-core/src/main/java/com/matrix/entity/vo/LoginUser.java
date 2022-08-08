package com.matrix.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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

    private List<String> permissions;

    private List<String> roles;

    public String getLoginId() {
        return userType + ":" + userId;
    }

}
