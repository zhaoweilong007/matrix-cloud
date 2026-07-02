package com.matrix.redis.ratelimiter;

import cn.hutool.extra.spring.SpringUtil;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.redis.utils.RedisUtils;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Redis 令牌桶限流切面
 *
 */
@Slf4j
@Aspect
public class RateLimiterAspect {

    private static final String RATE_LIMITER_KEY_PREFIX = "rate_limiter:";
    private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();
    private static final DefaultParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    @Around("@annotation(rateLimiter)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimiter rateLimiter) throws Throwable {
        String key = buildKey(joinPoint, rateLimiter);
        double permitsPerSecond = rateLimiter.permitsPerSecond();
        long timeoutMs = rateLimiter.timeout() * 1000;

        // 令牌桶 key
        String bucketKey = RATE_LIMITER_KEY_PREFIX + key;
        // 使用 Redis 实现简单令牌桶
        if (!tryAcquire(bucketKey, permitsPerSecond, timeoutMs)) {
            throw new ServiceException(SystemErrorTypeEnum.OPERATE_FAIL, rateLimiter.message());
        }

        return joinPoint.proceed();
    }

    private String buildKey(ProceedingJoinPoint joinPoint, RateLimiter rateLimiter) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        return switch (rateLimiter.keyType()) {
            case GLOBAL -> methodName;
            case USER -> {
                try {
                    String userId = getLoginUserId();
                    yield methodName + ":user:" + userId;
                } catch (Exception e) {
                    yield methodName + ":user:anonymous";
                }
            }
            case IP -> methodName + ":ip:" + getClientIp();
            case SERVER_NODE -> methodName + ":node:" + getServerNode();
            case CUSTOM -> methodName + ":custom:" + parseSpelKey(joinPoint, rateLimiter.key());
        };
    }

    private String parseSpelKey(ProceedingJoinPoint joinPoint, String spelExpression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        StandardEvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = NAME_DISCOVERER.getParameterNames(method);
        Object[] args = joinPoint.getArgs();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        return SPEL_PARSER.parseExpression(spelExpression).getValue(context, String.class);
    }

    private boolean tryAcquire(String bucketKey, double permitsPerSecond, long timeoutMs) {
        long now = System.currentTimeMillis();
        String lastTimeKey = bucketKey + ":last";
        String tokensKey = bucketKey + ":tokens";

        Long lastTime = (Long) RedisUtils.getCacheObject(lastTimeKey);
        double tokens = 0;
        Object tokensObj = RedisUtils.getCacheObject(tokensKey);
        if (tokensObj != null) {
            tokens = ((Number) tokensObj).doubleValue();
        }

        if (lastTime == null) {
            lastTime = now;
        }

        long elapsed = now - lastTime;
        tokens = Math.min(permitsPerSecond, tokens + elapsed * permitsPerSecond / 1000.0);

        if (tokens >= 1.0) {
            tokens -= 1.0;
            RedisUtils.setCacheObject(lastTimeKey, now, Duration.ofSeconds((long) Math.ceil(permitsPerSecond)));
            RedisUtils.setCacheObject(tokensKey, tokens, Duration.ofSeconds((long) Math.ceil(permitsPerSecond)));
            return true;
        }

        // 等待指定时间
        if (timeoutMs > 0 && tokens > 0) {
            return true;
        }

        return false;
    }

    private String getClientIp() {
        try {
            jakarta.servlet.http.HttpServletRequest request =
                    com.matrix.common.util.servlet.ServletUtils.getRequest();
            return com.matrix.common.util.servlet.ServletUtils.getClientIP(request);
        } catch (Exception e) {
            return UUID.randomUUID().toString();
        }
    }

    private String getLoginUserId() {
        try {
            Class<?> stpUtil = Class.forName("cn.dev33.satoken.stp.StpUtil");
            return (String) stpUtil.getMethod("getLoginIdAsString").invoke(null);
        } catch (Exception e) {
            return "anonymous";
        }
    }

    private String getServerNode() {
        try {
            return SpringUtil.getProperty("spring.cloud.client.ip-address")
                    + ":" + SpringUtil.getProperty("server.port");
        } catch (Exception e) {
            return "unknown";
        }
    }
}
