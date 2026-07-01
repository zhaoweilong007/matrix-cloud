package com.matrix.auth.sign;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 签名校验注解
 * <p>
 * 基于 appId + timestamp + nonce + sign 的防篡改 + 防重放机制
 * </p>
 *
 * @author matrix
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiSignature {

    /**
     * 签名有效期（秒），默认 300 秒
     */
    long timeout() default 300;

    /**
     * App ID 参数名
     */
    String appIdParam() default "appId";

    /**
     * 时间戳参数名
     */
    String timestampParam() default "timestamp";

    /**
     * 随机数参数名
     */
    String nonceParam() default "nonce";

    /**
     * 签名参数名
     */
    String signParam() default "sign";
}
