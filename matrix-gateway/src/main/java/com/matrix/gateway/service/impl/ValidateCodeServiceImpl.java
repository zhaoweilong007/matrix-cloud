package com.matrix.gateway.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import com.matrix.auto.enums.CaptchaType;
import com.matrix.auto.properties.CaptchaProperties;
import com.matrix.common.constant.CacheConstants;
import com.matrix.common.constant.Constants;
import com.matrix.common.exception.CaptchaException;
import com.matrix.common.result.R;
import com.matrix.common.util.SpringUtils;
import com.matrix.common.util.StringUtils;
import com.matrix.common.util.reflect.ReflectUtils;
import com.matrix.gateway.service.ValidateCodeService;
import com.matrix.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码实现处理
 */
@RequiredArgsConstructor
public class ValidateCodeServiceImpl implements ValidateCodeService {

    private final CaptchaProperties captchaProperties;

    /**
     * 生成验证码
     */
    @Override
    public Mono<R<Map<String, String>>> createCaptcha() {
        Map<String, String> ajax = new HashMap<>();
        boolean captchaEnabled = captchaProperties.getEnabled();
        if (!captchaEnabled) {
            return Mono.just(R.success(ajax));
        }

        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();
        CodeGenerator codeGenerator = ReflectUtils.newInstance(captchaType.getClazz(), length);
        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }

        return Mono.just(RedisUtils.set(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION)))
                .flatMap(success -> {
                    if (success) {
                        return Mono.just(R.success(Map.of("uuid", uuid, "img", captcha.getImageBase64())));
                    } else {
                        return Mono.error(new CaptchaException("获取验证码失败"));
                    }
                })
                .onErrorResume(throwable -> Mono.just(R.fail("获取验证码失败")));

    }
}
