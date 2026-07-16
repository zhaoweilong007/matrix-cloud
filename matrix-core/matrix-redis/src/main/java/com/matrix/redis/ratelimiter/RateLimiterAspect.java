package com.matrix.redis.ratelimiter;

import cn.hutool.extra.spring.SpringUtil;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import com.matrix.common.model.login.LoginUser;
import com.matrix.redis.utils.RedisUtils;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RateType;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Redis 令牌桶限流切面
 * <p>使用 Redisson 原生 {@link org.redisson.api.RRateLimiter} 实现原子限流。</p>
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
        String bucketKey = RATE_LIMITER_KEY_PREFIX + key;
        // 使用 Redisson 原生 RRateLimiter（原子操作，线程安全，替代原非原子两步 Redis 操作）
        long available = RedisUtils.rateLimiter(
                bucketKey,
                RateType.OVERALL,
                (int) rateLimiter.permitsPerSecond(),
                1
        );
        if (available == -1) {
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

    private String getClientIp() {
        try {
            jakarta.servlet.http.HttpServletRequest request =
                    com.matrix.common.util.servlet.ServletUtils.getRequest();
            return com.matrix.common.util.servlet.ServletUtils.getClientIP(request);
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String getLoginUserId() {
        // 直接从 TTL 上下文获取，避免反射调用 StpUtil
        LoginUser user = LoginUserContextHolder.getUser();
        return user != null ? String.valueOf(user.getUserId()) : "anonymous";
    }

    private String getServerNode() {
        // 使用默认值避免 NullPointerException
        String ip = SpringUtil.getProperty("spring.cloud.client.ip-address", "127.0.0.1");
        String port = SpringUtil.getProperty("server.port", "8080");
        return ip + ":" + port;
    }
}
