package com.matrix.crypto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 加解密注解
 * <p>
 * 标记在 Controller 方法或类上，表示请求需要解密、响应需要加密
 * </p>
 *
 * @author matrix
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiEncrypt {

    /**
     * 是否解密请求
     */
    boolean decryptRequest() default true;

    /**
     * 是否加密响应
     */
    boolean encryptResponse() default true;
}
