package com.matrix.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.matrix.api.system.entity.dto.SysAdminLoginDto;
import com.matrix.api.system.entity.dto.SysAdminRegisterDto;
import com.matrix.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 描述：登录控制器
 *
 * @author zwl
 * @since 2022/7/8 14:47
 **/
@RestController
@Slf4j
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * 注册
     * @return SaResult
     */
    @PostMapping("/register")
    public SaResult register(@Validated @RequestBody SysAdminRegisterDto sysAdminRegisterDto) {
        String username = loginService.register(sysAdminRegisterDto);
        return SaResult.data(username);
    }

    /**
     * 登录
     *
     * @return SaResult
     */
    @PostMapping("/login")
    public SaResult login(@Validated @RequestBody SysAdminLoginDto sysAdminLoginDto) {
        return SaResult.data(loginService.login(sysAdminLoginDto));
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }


}
