package com.matrix.log.aspect;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Maps;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.context.TenantContextHolder;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.result.R;
import com.matrix.common.util.SpringUtils;
import com.matrix.common.util.StringUtils;
import com.matrix.common.util.VUtils;
import com.matrix.common.util.servlet.ServletUtils;
import com.matrix.log.annotation.Log;
import com.matrix.log.api.client.RemoteUserService;
import com.matrix.log.enums.BusinessStatus;
import com.matrix.log.event.OperLogEvent;
import com.matrix.log.utils.AddressUtils;
import com.matrix.prometheus.util.TracerUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * 操作日志记录处理
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class LogAspect {

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPwd", "newPwd"};
    private final RemoteUserService remoteUserService;

    public static String obtainMethodArgs(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] argNames = methodSignature.getParameterNames();
        Object[] argValues = joinPoint.getArgs();
        // 拼接参数
        Map<String, Object> args = Maps.newHashMapWithExpectedSize(argValues.length);
        for (int i = 0; i < argNames.length; i++) {
            String argName = argNames[i];
            Object argValue = argValues[i];
            // 被忽略时，标记为 ignore 字符串，避免和 null 混在一起
            args.put(argName, !isIgnoreArgs(argValue) ? argValue : "[ignore]");
        }
        return JSON.toJSONString(args);
    }

    private static boolean isIgnoreArgs(Object object) {
        Class<?> clazz = object.getClass();
        // 处理数组的情况
        if (clazz.isArray()) {
            return IntStream.range(0, Array.getLength(object))
                    .anyMatch(index -> isIgnoreArgs(Array.get(object, index)));
        }
        // 递归，处理数组、Collection、Map 的情况
        if (Collection.class.isAssignableFrom(clazz)) {
            return ((Collection<?>) object).stream()
                    .anyMatch((Predicate<Object>) LogAspect::isIgnoreArgs);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return isIgnoreArgs(((Map<?, ?>) object).values());
        }
        // obj
        if (object instanceof MultipartFile
                || object instanceof HttpServletRequest
                || object instanceof HttpServletResponse
                || object instanceof BindingResult) {
            return true;
        }
        //排除敏感字段
        try {
            final JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(object));
            MapUtil.removeAny(jsonObject, EXCLUDE_PROPERTIES);
        } catch (Exception e) {
            log.error("日志脱敏处理异常：{}", e.getMessage());
        }
        return false;
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // *========数据库日志=========*//
            OperLogEvent operLog = new OperLogEvent();
            operLog.setUserId(Optional.ofNullable(LoginUserContextHolder.getUser()).map(LoginUser::getUserId).orElseGet(ServletUtils::getUserIdByRequestHead));
            operLog.setTenantId(TenantContextHolder.getTenantId());
            operLog.setTraceId(TracerUtils.getTraceId());
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            final String ip = ServletUtils.getClientIP();
            operLog.setOperIp(ip);
            operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            operLog.setOperName(getUserName());
            operLog.setOperLocation(AddressUtils.getRealAddressByIP(ip));
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            // 发布事件保存数据库
            SpringUtils.context().publishEvent(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("系统日志写入异常:{}", exp.getMessage());
        }
    }

    private String getUserName() {
        return Optional.ofNullable(LoginUserContextHolder.getUser()).map(LoginUser::getUsername).orElseGet(() -> {
            Long userId = ServletUtils.getUserIdByRequestHead();
            if (userId != null) {
                final R<String> res = remoteUserService.selectAuditUserByUserId(userId);
                if (VUtils.checkRes(res)) {
                    return res.getData();
                }
            }
            return null;
        });
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperLogEvent operLog, Object jsonResult) throws Exception {
        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && ObjectUtil.isNotNull(jsonResult)) {
            operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, OperLogEvent operLog, String[] excludeParamNames) throws Exception {
        final HttpServletRequest request = ServletUtils.getRequest();
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.GET.name().equals(requestMethod) || HttpMethod.DELETE.name().equals(requestMethod)) {
            Map<String, String> paramsMap = ServletUtils.getParamMap(request);
            operLog.setOperParam(JSON.toJSONString(paramsMap));
        } else {
            operLog.setOperParam(obtainMethodArgs(joinPoint));
        }
    }


}
