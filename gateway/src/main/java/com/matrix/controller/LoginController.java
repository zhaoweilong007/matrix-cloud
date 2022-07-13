package com.matrix.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.matrix.entity.dto.SysAdminLoginDto;
import com.matrix.entity.dto.SysAdminRegisterDto;
import com.matrix.service.SysAdminService;
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

    private final SysAdminService sysAdminService;

    /**
     * @param sysAdminRegisterDto 登录参数
     * @return SaResult
     */
    @PostMapping("register")
    public Mono<SaResult> register(@Validated @RequestBody SysAdminRegisterDto sysAdminRegisterDto) {
        String username = sysAdminService.register(sysAdminRegisterDto);
        return Mono.just(SaResult.data(username));
    }


    /**
     * 登录
     *
     * @return SaResult
     */
    @PostMapping("login")
    public Mono<SaResult> login(@Validated @RequestBody SysAdminLoginDto sysAdminLoginDto) {
        return Mono.just(SaResult.data(sysAdminService.login(sysAdminLoginDto)));
    }

    /**
     * 退出登录
     */
    @GetMapping("logout")
    public Mono<SaResult> logout() {
        StpUtil.logout();
        return Mono.just(SaResult.ok());
    }


    @GetMapping("userInfo")
    public Mono<SaResult> getUserInfo() {
        Long loginId = (Long) StpUtil.getLoginId();
        return Mono.just(SaResult.data(sysAdminService.userInfo(loginId)));
    }

}
