package com.matrix.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.matrix.entity.dto.SysAdminDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 描述：登录控制器
 *
 * @author zwl
 * @since 2022/7/8 14:47
 **/
@RestController
@Slf4j
@RequestMapping("/user")
@Valid
@RequiredArgsConstructor
public class LoginController {

    /**
     * @param sysAdminDto 登录参数
     * @return SaResult
     */
    @PostMapping("/register")
    public SaResult register(@RequestBody SysAdminDto sysAdminDto) {
        return SaResult.ok();
    }

    @RequestMapping("login")
    public SaResult login() {
        StpUtil.login(10001);
        return SaResult.ok("登录成功");
    }

    @RequestMapping("isLogin")
    public SaResult isLogin() {
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }


}
