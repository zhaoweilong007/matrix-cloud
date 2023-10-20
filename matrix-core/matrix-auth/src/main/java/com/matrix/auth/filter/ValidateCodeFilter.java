package com.matrix.auth.filter;

import cn.dev33.satoken.router.SaRouter;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.matrix.auto.properties.CaptchaProperties;
import com.matrix.common.constant.CacheConstants;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.CaptchaException;
import com.matrix.common.exception.CaptchaExpireException;
import com.matrix.common.exception.base.BaseException;
import com.matrix.common.result.R;
import com.matrix.common.util.StringUtils;
import com.matrix.common.util.json.JsonUtils;
import com.matrix.common.util.servlet.ServletUtils;
import com.matrix.redis.utils.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 验证码校验
 *
 * @author ZhaoWeiLong
 * @since 2023/9/23
 **/
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private static final String CODE = "code";
    private static final String UUID = "uuid";
    private final CaptchaProperties captchaProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String path = request.getRequestURI();

        if (!SaRouter.isMatch(captchaProperties.getValidateUrl(), path)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            String rspStr = ServletUtils.getBody(request);
            Dict obj = JsonUtils.parseMap(rspStr);
            checkCaptcha(obj.getStr(CODE), obj.getStr(UUID));
        } catch (Exception e) {
            if (e instanceof BaseException baseException) {
                ServletUtils.writeJSON(response, R.fail(SystemErrorTypeEnum.VERIFICATION_CODE_ERROR, baseException.getMessage()));
            } else {
                ServletUtils.writeJSON(response, R.fail(SystemErrorTypeEnum.VERIFICATION_CODE_ERROR));
            }
            return;
        }
        chain.doFilter(request, response);
    }

    public void checkCaptcha(String code, String uuid) {
        if (StringUtils.isEmpty(code)) {
            throw new CaptchaException("验证码参数code不能为空");
        }
        if (StringUtils.isEmpty(uuid)) {
            throw new CaptchaException("验证码参数uuid不能为空");
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        final Object cacheCode = RedisUtils.getCacheObject(verifyKey);

        if (cacheCode == null) {
            throw new CaptchaExpireException();
        }

        if (!StrUtil.equalsIgnoreCase(code, cacheCode.toString())) {
            throw new CaptchaException();
        }
    }

}
