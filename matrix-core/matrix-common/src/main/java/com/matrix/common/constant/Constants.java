package com.matrix.common.constant;

/**
 * 通用常量信息
 */
public interface Constants {

    /**
     * 验证码有效期（分钟）
     */
    long CAPTCHA_EXPIRATION = 2;

    /**
     * 防重提交 redis key
     */
    String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 忽略租户标识
     */
    String IGNORE_TENANT = "ignore_tenant";

    /**
     * Bearer 认证前缀
     */
    String BEARER = "Bearer ";

    /**
     * 版本号
     */
    String VERSION = "VERSION";

    /**
     * 默认版本号
     */
    String DEFAULT_VERSION = "default";

}
