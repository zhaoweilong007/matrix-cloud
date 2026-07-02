package com.matrix.auth.strategy;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.matrix.common.enums.BusinessErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.model.login.LoginUser;
import com.matrix.redis.utils.RedisUtils;
import org.springframework.stereotype.Component;

/**
 * 短信验证码登录认证策略。
 *
 * <p>从请求体中解析 {@code phone} 和 {@code code}，
 * 验证 Redis 中的短信验证码后返回 {@link LoginUser}。</p>
 */
@Component("sms" + IAuthStrategy.BASE_NAME)
public class SmsAuthStrategy implements IAuthStrategy {

    /** 短信验证码 Redis Key 前缀 */
    private static final String SMS_CODE_PREFIX = "sms:code:";

    /**
     * 短信验证码登录认证。
     */
    @Override
    public LoginUser authenticate(String body) {
        JSONObject json = JSON.parseObject(body);
        String phone = json.getString("phone");
        String code = json.getString("code");

        if (phone == null || code == null) {
            throw new ServiceException(BusinessErrorTypeEnum.USER_MOBILE_EXIST);
        }

        // 验证短信验证码
        String cachedCode = RedisUtils.getCacheObject(SMS_CODE_PREFIX + phone);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw new ServiceException(BusinessErrorTypeEnum.AUTHENTICATION_FAILED);
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setMobile(phone);
        return loginUser;
    }
}
