package com.matrix.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.matrix.entity.dto.SysAdminLoginDto;
import com.matrix.entity.dto.SysAdminRegisterDto;
import com.matrix.entity.vo.SysAdminUserInfo;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/14 14:33
 **/
public interface LoginService {

    /**
     * 注册功能
     */
    String register(SysAdminRegisterDto umsAdminParam);

    /**
     * 登录功能
     */
    SaTokenInfo login(SysAdminLoginDto sysAdminLoginDto);


    /**
     * 刷新token的功能
     */
    String refreshToken(String oldToken);



}
