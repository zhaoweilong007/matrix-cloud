package com.matrix.auth.sign;

import cn.hutool.crypto.SecureUtil;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.redis.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * API 签名校验切面
 *
 * @author matrix
 */
@Slf4j
@Aspect
public class ApiSignatureAspect {

    private static final String NONCE_CACHE_PREFIX = "api_signature:nonce:";

    @Before("@annotation(apiSignature)")
    public void verify(ApiSignature apiSignature) {
        HttpServletRequest request = getRequest();

        String appId = request.getParameter(apiSignature.appIdParam());
        String timestamp = request.getParameter(apiSignature.timestampParam());
        String nonce = request.getParameter(apiSignature.nonceParam());
        String sign = request.getParameter(apiSignature.signParam());

        // 1. 参数完整性校验
        if (!StringUtils.hasText(appId) || !StringUtils.hasText(timestamp)
                || !StringUtils.hasText(nonce) || !StringUtils.hasText(sign)) {
            throw new ServiceException(SystemErrorTypeEnum.PARAM_ERROR, "签名参数不完整");
        }

        // 2. 时间戳有效性校验（防重放）
        long now = System.currentTimeMillis() / 1000;
        long clientTime;
        try {
            clientTime = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            throw new ServiceException(SystemErrorTypeEnum.PARAM_ERROR, "时间戳格式错误");
        }
        if (Math.abs(now - clientTime) > apiSignature.timeout()) {
            throw new ServiceException(SystemErrorTypeEnum.PARAM_ERROR, "请求已过期");
        }

        // 3. Nonce 防重放（Redis 缓存）
        String nonceKey = NONCE_CACHE_PREFIX + nonce;
        if (!RedisUtils.setObjectIfAbsent(nonceKey, "1", Duration.ofSeconds(apiSignature.timeout() + 60))) {
            throw new ServiceException(SystemErrorTypeEnum.PARAM_ERROR, "请求已处理");
        }

        // 4. 签名校验
        Map<String, String[]> paramMap = request.getParameterMap();
        TreeMap<String, String> sortedParams = new TreeMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            if (!apiSignature.signParam().equals(entry.getKey())) {
                sortedParams.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        // 拼接签名字符串
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        // 追加 appSecret（实际应从数据库或配置获取）
        String appSecret = getAppSecret(appId);
        sb.append("secret=").append(appSecret);

        String expectedSign = SecureUtil.md5(sb.toString()).toUpperCase();
        if (!expectedSign.equals(sign.toUpperCase())) {
            throw new ServiceException(SystemErrorTypeEnum.PARAM_ERROR, "签名校验失败");
        }
    }

    private String getAppSecret(String appId) {
        // TODO: 从数据库或配置中心获取 appId 对应的密钥
        return "default-secret-" + appId;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new ServiceException(SystemErrorTypeEnum.PARAM_ERROR, "无法获取请求信息");
        }
        return attributes.getRequest();
    }
}
