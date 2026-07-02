package com.matrix.log.event;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * 异常事件对象，用于封装异常报警信息。
 * <p>
 * 当系统发生异常时，发布该事件可携带报警主题、应用名称、接口路径、
 * 跟踪号、异常提示及堆栈信息，由监听器处理并发送通知。
 */
@Data
@Builder
public class ExceptionEvent implements Serializable {

    /**
     * 报警主题
     */
    private String title;

    /**
     * 报警应用
     */
    private String application;

    /**
     * 报警接口
     */
    private String apiPath;

    /**
     * 报警跟踪号
     */
    private String traceId;

    /**
     * 报警提示
     */
    private String message;

    /**
     * 报警堆栈
     */
    private String stackTrace;
}
