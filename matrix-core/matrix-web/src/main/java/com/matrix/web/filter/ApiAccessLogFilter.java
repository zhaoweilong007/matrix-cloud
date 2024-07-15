package com.matrix.web.filter;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.context.TerminalContextHolder;
import com.matrix.common.enums.PlatformUserTypeEnum;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.result.R;
import com.matrix.common.util.TracerUtils;
import com.matrix.common.util.json.JsonUtils;
import com.matrix.common.util.servlet.ServletUtils;
import com.matrix.web.client.ApiAccessLogApi;
import com.matrix.web.model.ApiAccessLog;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Async;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author ZhaoWeiLong
 * @since 2024/2/23
 **/
@Slf4j
public class ApiAccessLogFilter extends OncePerRequestFilter {


  private final String applicationName;

  private final ApiAccessLogApi apiAccessLogApi;
  private final String[] ignoreUrl = {"/actuator", "/apiAccessLog"};

  public ApiAccessLogFilter(String applicationName, ApiAccessLogApi apiAccessLogApi) {
    this.applicationName = applicationName;
    this.apiAccessLogApi = apiAccessLogApi;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    final String requestURI = request.getRequestURI();
    if (StrUtil.startWithAny(requestURI, ignoreUrl)) {
      filterChain.doFilter(request, response);
      return;
    }
    LocalDateTime beginTime = LocalDateTime.now();
    Map<String, String> queryString = ServletUtils.getParamMap(request);
    String requestBody = ServletUtils.isJsonRequest(request) ? ServletUtils.getBody(request) : null;
    try {
      filterChain.doFilter(request, response);
      createApiAccessLog(request, beginTime, queryString, requestBody, null);
    } catch (Exception ex) {
      createApiAccessLog(request, beginTime, queryString, requestBody, ex);
      throw ex;
    }
  }

  private void createApiAccessLog(HttpServletRequest request, LocalDateTime beginTime,
      Map<String, String> queryString, String requestBody, Exception ex) {
    ApiAccessLog accessLog = new ApiAccessLog();
    try {
      this.buildApiAccessLogDTO(accessLog, request, beginTime, queryString, requestBody, ex);
      Async.runAsync(() -> apiAccessLogApi.createApiAccessLog(accessLog));
    } catch (Throwable th) {
      log.error("[createApiAccessLog][url({}) log({}) 发生异常]", request.getRequestURI(),
          JsonUtils.toJsonString(accessLog), th);
    }
  }

  private void buildApiAccessLogDTO(ApiAccessLog accessLog, HttpServletRequest request, LocalDateTime beginTime,
      Map<String, String> queryString, String requestBody, Exception ex) {
    // 处理用户信息
    final LoginUser user = LoginUserContextHolder.getUser();
    if (user != null) {
      accessLog.setUserId(user.getUserId());
    }
    final PlatformUserTypeEnum userType = TerminalContextHolder.getUserType();
    if (userType != null) {
      accessLog.setUserType(userType.getValue());

    }
    // 设置访问结果
    R<?> result = ServletUtils.getCommonResult(request);
    if (result != null) {
      accessLog.setResultCode(result.getCode());
      accessLog.setResultMsg(result.getMessage());
    } else if (ex != null) {
      accessLog.setResultCode(SystemErrorTypeEnum.SYSTEM_ERROR.getCode());
      accessLog.setResultMsg(ExceptionUtil.getRootCauseMessage(ex));
    } else {
      accessLog.setResultCode(0);
      accessLog.setResultMsg("");
    }
    // 设置其它字段
    accessLog.setTraceId(TracerUtils.getTraceId());
    accessLog.setApplicationName(applicationName);
    accessLog.setRequestUrl(request.getRequestURI());
    Map<String, Object> requestParams = MapUtil.<String, Object>builder().put("query", queryString)
        .put("body", requestBody).build();
    accessLog.setRequestParams(JsonUtils.toJsonString(requestParams));
    accessLog.setRequestMethod(request.getMethod());
    accessLog.setUserAgent(ServletUtils.getUserAgent(request));
    accessLog.setUserIp(ServletUtils.getClientIP(request));
    // 持续时间
    accessLog.setBeginTime(beginTime);
    accessLog.setEndTime(LocalDateTime.now());
    accessLog.setDuration(
        (int) LocalDateTimeUtil.between(accessLog.getBeginTime(), accessLog.getEndTime(), ChronoUnit.MILLIS));
  }

}
