package com.matrix.log.event;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.matrix.auto.properties.ExceptionNoticeProperties;
import com.matrix.common.entity.SysLoginLog;
import com.matrix.common.entity.SysOperateLog;
import com.matrix.log.api.client.RemoteLogService;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步调用日志服务
 */
@Slf4j
@RequiredArgsConstructor
public class LogEventListener {

    private final ExceptionNoticeProperties exceptionNoticeProperties;
    private final RemoteLogService remoteLogService;
    private final Converter converter;

    /**
     * 保存系统日志记录
     */
    @Async
    @EventListener
    public void saveLog(OperLogEvent operLogEvent) {
        SysOperateLog sysOperLog = converter.convert(operLogEvent, SysOperateLog.class);
        remoteLogService.saveLog(sysOperLog);
    }

    /**
     * 保存登录日志
     */
    @Async
    @EventListener
    public void saveLogininfor(LogininforEvent logininforEvent) {
        SysLoginLog loginLog = converter.convert(logininforEvent, SysLoginLog.class);
        remoteLogService.saveLoginLog(loginLog);
    }


    /**
     * 消费异常事件
     *
     * @param event 异常对象
     */
    @EventListener({ExceptionEvent.class})
    @Async
    public void exceptionNotice(ExceptionEvent event) {
        HttpResponse httpResponse = null;
        try {
            final String msg = String.format(ExceptionNoticeProperties.MSG_TEMPLATE, event.getApplication(), event.getApiPath(), DateUtil.now(),
                    event.getTraceId(), event.getMessage(), event.getStackTrace());
            httpResponse = HttpRequest.post(exceptionNoticeProperties.getAlertUrl()).body(msg).executeAsync();
        } catch (Exception e) {
            log.error("ExceptionEvent send msg error:{}", httpResponse);
        }
    }
}
