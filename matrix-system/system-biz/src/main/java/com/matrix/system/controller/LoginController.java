package com.matrix.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import com.matrix.api.system.entity.dto.SysAdminLoginDto;
import com.matrix.api.system.entity.dto.SysAdminRegisterDto;
import com.matrix.common.constant.CacheConstants;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.result.R;
import com.matrix.redis.utils.RedisUtils;
import com.matrix.system.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登录控制器。
 *
 * <p>提供验证码获取、注册、登录、退出功能。</p>
 */
@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "登录服务")
public class LoginController {

    private final LoginService loginService;

    /**
     * 获取图片验证码。
     *
     * @return 验证码 uuid 和 Base64 图片
     */
    @GetMapping("/captcha")
    @Operation(summary = "获取验证码")
    public R<Object> captcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(130, 48);
        String code = captcha.getCode();
        String uuid = UUID.fastUUID().toString(true);
        RedisUtils.setCacheObject(CacheConstants.CAPTCHA_CODE_KEY + uuid, code, Duration.ofMinutes(1));
        return R.success().setData(new HashMap<String, Object>() {{
            put("uuid", uuid);
            put("img", captcha.getImageBase64());
        }});
    }

    /**
     * 用户注册。
     *
     * @param sysAdminRegisterDto 注册信息
     * @return 注册成功的用户名
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R<String> register(@Validated @RequestBody SysAdminRegisterDto sysAdminRegisterDto) {
        String username = loginService.register(sysAdminRegisterDto);
        return R.success(username);
    }

    /**
     * 账号密码登录。
     *
     * @param sysAdminLoginDto 登录信息
     * @return 登录 Token
     */
    @PostMapping("/login")
    @Operation(summary = "账号密码登录")
    public R<Object> login(@Validated @RequestBody SysAdminLoginDto sysAdminLoginDto) {
        String redisCode = RedisUtils.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + sysAdminLoginDto.getUuid());
        if (!Objects.equals(redisCode, sysAdminLoginDto.getCode())) {
            return R.fail(SystemErrorTypeEnum.VERIFICATION_CODE_ERROR);
        }
        return R.success(loginService.login(sysAdminLoginDto));
    }

    /**
     * 退出登录。
     */
    @GetMapping("/logout")
    @Operation(summary = "退出登录")
    public R<Void> logout() {
        StpUtil.logout();
        return R.success();
    }
}
