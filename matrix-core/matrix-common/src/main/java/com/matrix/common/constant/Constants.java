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

    String IGNORE_TENANT = "ignore_tenant";

    String BEARER = "Bearer ";

    String VERSION = "VERSION";

    String DEFAULT_VERSION = "default";

}
