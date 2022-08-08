package com.matrix.controller.admin;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import com.matrix.api.system.entity.dto.SysAdminLoginDto;
import com.matrix.api.system.entity.dto.SysAdminRegisterDto;
import com.matrix.component.RedisOps;
import com.matrix.entity.vo.CacheKey;
import com.matrix.entity.vo.Result;
import com.matrix.exception.SystemErrorType;
import com.matrix.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

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
@Api(tags = "登录服务")
public class LoginController {

    private final LoginService loginService;
    private final RedisOps redisOps;


    @GetMapping("/captcha")
    @ApiOperation("获取验证码")
    public Result<Object> captcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(130, 48);
        String code = captcha.getCode();
        String uuid = UUID.fastUUID().toString(true);
        redisOps.set(CacheKey.of(uuid, Duration.ofMinutes(1)), code, true);
        return Result.success().setData(new HashMap<String, Object>() {{
            put("uuid", uuid);
            put("img", captcha.getImageBase64());
        }});
    }

    /**
     * 注册
     *
     * @return Result
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<String> register(@Validated @RequestBody SysAdminRegisterDto sysAdminRegisterDto) {
        String username = loginService.register(sysAdminRegisterDto);
        return Result.success(username);
    }

    /**
     * 登录
     *
     * @return Result
     */
    @PostMapping("/login")
    @ApiOperation("账号密码登录")
    public Result<Object> login(@Validated @RequestBody SysAdminLoginDto sysAdminLoginDto) {
        String redisCode = redisOps.get(sysAdminLoginDto.getUuid());
        if (!Objects.equals(redisCode, sysAdminLoginDto.getCode())) {
            return Result.fail(SystemErrorType.VERIFICATION_CODE_ERROR);
        }
        return Result.success(loginService.login(sysAdminLoginDto));
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    @ApiOperation("退出登录")
    public Result logout() {
        StpUtil.logout();
        return Result.success();
    }


}
