package com.matrix.controller;

import cn.dev33.satoken.util.SaResult;
import com.matrix.entity.dto.SysAdminRegisterDto;
import com.matrix.entity.po.SysAdmin;
import com.matrix.service.SysAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 描述：登录控制器
 *
 * @author zwl
 * @since 2022/7/8 14:47
 **/
@RestController
@Slf4j
@RequestMapping("/auth")
@Valid
@RequiredArgsConstructor
public class LoginController {

    private final SysAdminService sysAdminService;

    /**
     * @param sysAdminRegisterDto 登录参数
     * @return SaResult
     */
    @PostMapping("register")
    public Mono<SaResult> register(@RequestBody SysAdminRegisterDto sysAdminRegisterDto) {
        SysAdmin sysAdmin = sysAdminService.register(sysAdminRegisterDto);
        return sysAdmin == null ? Mono.just(SaResult.error("注册失败，用户已存在")) : Mono.just(SaResult.data(sysAdmin));
    }

    @PostMapping("login")
    public Mono<SaResult> login(@NotBlank(message = "用户名不能为空") @RequestParam String username,
                                @NotBlank(message = "密码不呢为空") @RequestParam String password) {
        return Mono.just(SaResult.data(sysAdminService.login(username, password)));
    }


}
